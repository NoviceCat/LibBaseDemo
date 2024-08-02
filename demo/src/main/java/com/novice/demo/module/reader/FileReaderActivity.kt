package com.novice.demo.module.reader

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.novice.base.extension.safe
import com.novice.base.uicore.ui.BaseActivity
import com.novice.base.uicore.viewmodel.DefaultViewModel
import com.novice.demo.databinding.ActivityFileReaderBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.util.Scanner


class FileReaderActivity : BaseActivity<ActivityFileReaderBinding, DefaultViewModel>() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, FileReaderActivity::class.java))
        }
    }

    val list = ArrayList<String>()

    var filterKey = ""

    override fun initView() {
        setToolBarTitle("文件选择和过滤")
        mBinding.btnStart.setOnClickListener {
            filterKey = mBinding.edtInput.text.toString()
            if (filterKey.isBlank()){
                Toast.makeText(this,"先输入过滤条件再点击选择文件",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("*/*")
            startActivityForResult(intent, 1)
        }
        mBinding.btnOutput.setOnClickListener {
            if (list.isEmpty()){
                Toast.makeText(this,"没有可以导出的文件",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            exportListToTxt(this, list, "过滤条件为_$filterKey")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                val uri = data?.data!!
                val inputStream = contentResolver.openInputStream(uri)
                val keyList = filterKey.split(",")
                list.clear()
                lifecycleScope.launch(Dispatchers.Main) {
                    showProgressDialog()
                    withContext(Dispatchers.IO) {
                        if (keyList.size > 1) {
                            val scanner = Scanner(inputStream)
                            while (scanner.hasNextLine()) {
                                scanner.nextLine().let {
                                    for (key: String in keyList) {
                                        if (it.startsWith(key)) {
                                            list.add(it)
                                        }
                                    }
                                }
                            }
                        } else {
                            val singleKey = keyList[0]
                            val scanner = Scanner(inputStream)
                            while (scanner.hasNextLine()) {
                                scanner.nextLine().let {
                                    if (it.startsWith(singleKey)) {
                                        list.add(it)
                                    }
                                }
                            }
                        }
                    }

                    LogUtils.d(
                        "NoviceLog",
                        "总共有： " + list.size + " 条开头为 ：" + filterKey + " 的数据"
                    )
                    mBinding.tvFilter.text =
                        "总共有： " + list.size + " 条开头为 ：" + filterKey + " 的数据"
                    hideProgressDialog()
                }
            }
        }
    }

    private fun exportListToTxt(context: Context, list: List<String?>, fileName: String) {
        lifecycleScope.launch(Dispatchers.Main) {
            showProgressDialog()
            var status = false
            var errorMsg = ""
            val file = File(context.getExternalFilesDir(null), "$fileName.txt")
            withContext(Dispatchers.IO) {
                try {
                    val fos = FileOutputStream(file)
                    val writer = BufferedWriter(OutputStreamWriter(fos))
                    for (item in list) {
                        writer.write(item)
                        writer.newLine()
                    }
                    writer.close()
                    fos.close()
                    status = true
                } catch (e: Exception) {
                    LogUtils.d("NoviceLog", "文件导出错误 ： " + e.message.safe())
                    errorMsg = e.message.safe()
                    e.printStackTrace()
                }
            }
            mBinding.tvOut.text =
                if (status) "文件导出成功： " + file.absolutePath else "文件导出失败： $errorMsg"
            hideProgressDialog()
        }
    }

}