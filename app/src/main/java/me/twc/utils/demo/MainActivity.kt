package me.twc.utils.demo

import android.os.Bundle
import me.twc.utils.applySingleDebouncingAnyTime
import me.twc.utils.demo.databinding.ActMainBinding

/**
 * @author 唐万超
 * @date 2022/11/10
 */
class MainActivity:BaseActivity() {

    private val mBinding by lazy { ActMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        initListener()
    }

    //<editor-fold desc="初始化">
    private fun initListener() = mBinding.let {
        it.btnPermission.applySingleDebouncingAnyTime {
            PermissionActivity.show(this@MainActivity)
        }
    }
    //</editor-fold>
}