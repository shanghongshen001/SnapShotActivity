-  android打印 编辑并打印 word
-  
## 预览
![image](http://img.blog.csdn.net/20150921145705714)

android打印word 方式

- 使用free office 开源包加载word 编辑并打印。 
- 把word转成html 使用webView 编辑并输出图片打印

关于第一种 放弃了。主要是包有点大，研究成本比较高。

我这里使用了第二种方法实现

主要遇到技术问题：1就是html如何无痕转换成html。2就是如何对html中需要编辑的内容进行替换。直接在webView中编辑不太友好。

流程-》 1word=》html=》webView+（html+输出参数）=》Bitmap=》pdf =》打印
## 许可
[MIT](https://mit-license.org/)
