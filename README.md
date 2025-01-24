# Android 动态加载 .dex文件

java 代码编译成 dex文件,并被Android 项目动态加载,然后执行 java方法

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
