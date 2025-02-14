# Android 类加载器

android 中 java 代码编译并压缩后是dex文件,ClassLoader 可以加载 dex 中的类

## java 源码编译成 dex 文件

https://github.com/dioq/Android_study/blob/main/bytecode/README.md

## Android Studio 多dex文件支持 multidex support

Gradle配置
android {
...
defaultConfig {
// multidex support.
multiDexEnabled = false
}
...
}

multiDexEnabled:
true 支持多 dex 文件
false 所有 java 代码 只生成 一个 dex 文件
