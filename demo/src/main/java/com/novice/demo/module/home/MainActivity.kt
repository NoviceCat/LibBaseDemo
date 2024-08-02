package com.novice.demo.module.home

import com.blankj.utilcode.util.BarUtils
import com.novice.base.manager.GridLayoutManagerWrap
import com.novice.base.uicore.ui.BaseActivity
import com.novice.base.uicore.viewmodel.DefaultViewModel
import com.novice.demo.databinding.ActivityMainBinding
import com.novice.demo.module.aspectj.AspectActivity
import com.novice.demo.module.container.ContainerActivity
import com.novice.demo.module.customstatuslayout.CustomStatusLayout1Activity
import com.novice.demo.module.customstatuslayout.CustomStatusLayout2Activity
import com.novice.demo.module.dialog.CommonAlertDialogActivity
import com.novice.demo.module.hmspush.HMSPushActivity
import com.novice.demo.module.list.ListActivity
import com.novice.demo.module.pdfreader.PDFReaderActivity
import com.novice.demo.module.reader.FileReaderActivity
import com.novice.demo.module.statuslayout.StatusLayoutActivity
import com.novice.demo.module.viewpager2.DragActivity
import com.novice.demo.module.viewpager2.ViewPager2Activity
import com.novice.demo.module.web.CommonWebActivity
import com.novice.demo.module.widget.WidgetActivity

class MainActivity : BaseActivity<ActivityMainBinding, DefaultViewModel>() {

    override fun enabledVisibleToolBar() = false

    override fun initView() {
        setImmersionBar()
        initRecyclerView()
    }

    private fun setImmersionBar() {
        val params = mBinding.viewImmersion.layoutParams
        params.height = BarUtils.getStatusBarHeight()
        mBinding.viewImmersion.layoutParams = params
    }

    private fun initRecyclerView() {
        val adapter = MainAdapter()
        val list = mutableListOf(
//            "基础控件", "CommonAlertDialog", "StatusLayout", "自定义StatusLayout", "完全自定义StatusLayout",
//            "指定StatusLayout覆盖区域", "SimpleList", "CommonWeb",
//            "ViewPager2", "drag","AspectJ","PDFReader",
//            "HMSPush"
            "过滤器"
        )
        mBinding.recyclerView.adapter = adapter
        mBinding.recyclerView.layoutManager = GridLayoutManagerWrap(this, 2)
        adapter.setNewInstance(list)
        adapter.setOnItemClickListener { _, _, position ->
            when (adapter.getItem(position)) {
                "基础控件" -> {
                    WidgetActivity.start(this)
                }
                "CommonAlertDialog" -> {
                    CommonAlertDialogActivity.start(this)
                }
                "StatusLayout" -> {
                    StatusLayoutActivity.start(this)
                }
                "自定义StatusLayout" -> {
                    CustomStatusLayout1Activity.start(this)
                }
                "完全自定义StatusLayout" -> {
                    CustomStatusLayout2Activity.start(this)
                }
                "指定StatusLayout覆盖区域" -> {
                    ContainerActivity.start(this)
                }
                "SimpleList" -> {
                    ListActivity.start(this)
                }
                "CommonWeb" -> {
                    CommonWebActivity.start(this, "https://www.baidu.com/")
                }
                "ViewPager2" -> {
                    ViewPager2Activity.start(this)
                }
                "drag" -> {
                    DragActivity.start(this)
                }
                "AspectJ" -> {
                    AspectActivity.start(this)
                }
                "PDFReader" ->{
                    PDFReaderActivity.start(this)
                }
                "HMSPush" ->{
                    HMSPushActivity.start(this)
                }
                "过滤器" ->{
                    FileReaderActivity.start(this)

                }
                else ->{}
            }
        }
    }

}
