package me.twc.utils.demo

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import me.twc.utils.applySingleDebouncingAnyTime
import me.twc.utils.demo.databinding.ActPermissionBinding
import me.twc.utils.registerForPermissions
import me.twc.utils.showCenterToast

/**
 * @author 唐万超
 * @date 2022/11/10
 */
class PermissionActivity : BaseActivity() {
    companion object {
        fun show(context: Context) {
            val intent = Intent(context, PermissionActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mBinding by lazy { ActPermissionBinding.inflate(layoutInflater) }
    private val mSinglePermissionLauncher = registerForPermissions(arrayOf(Manifest.permission.CAMERA)){
        showCenterToast("获取单权限成功(无提示)")
    }
    private val mSinglePermissionAlterLauncher = registerForPermissions(arrayOf(Manifest.permission.CAMERA),"给权限"){
        showCenterToast("获取单权限成功(有提示)")
    }
    private val mMultiPermissionLauncher = registerForPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_SMS)){
        showCenterToast("获取多权限成功(无提示)")
    }
    private val mMultiPermissionAlterLauncher = registerForPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_SMS),"给权限"){
        showCenterToast("获取多权限成功(有提示)")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        initListener()
    }

    //<editor-fold desc="初始化">
    private fun initListener() = mBinding.let {
        it.btnSingle.applySingleDebouncingAnyTime {
            mSinglePermissionLauncher.launch(this@PermissionActivity)
        }
        it.btnSingleAlter.applySingleDebouncingAnyTime {
            mSinglePermissionAlterLauncher.launch(this@PermissionActivity)
        }
        it.btnMulti.applySingleDebouncingAnyTime {
            mMultiPermissionLauncher.launch(this@PermissionActivity)
        }
        it.btnMultiAlter.applySingleDebouncingAnyTime {
            mMultiPermissionAlterLauncher.launch(this@PermissionActivity)
        }
    }
    //</editor-fold>
}