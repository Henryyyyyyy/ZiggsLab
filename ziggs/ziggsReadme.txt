#GreenDao工具类使用：
1.注意，gradle版本希望是3.5-all以上
2.在app.gradle中，加上 greendao { schemaVersion 1 }(与android同级)
3.加上apply plugin: 'org.greenrobot.greendao'
4.
// In your root build.gradle file:
buildscript {
    repositories {
        jcenter()
        mavenCentral() // add repository
    }
    dependencies {
       // classpath 'com.android.tools.build:gradle:2.3.3'
        classpath 'org.greenrobot:greendao-gradle-plugin:3.2.2' // add plugin
    }
}


