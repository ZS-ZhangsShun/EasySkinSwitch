# EasySkinSwitch
## 简介

    1、封装插件化换肤的核心代码
    2、兼容Android9.0以上版本
    3、详细介绍参考博客：https://blog.csdn.net/u013347784/article/details/122713344

## 使用方法（可参考app工程）

### 第一步：在project的build.gradle 文件中添加JitPack依赖

    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }

### 第二步: 在Module的build.gradle文件中添加对本库的依赖

    dependencies {
        ...
        implementation 'com.github.ZS-ZhangsShun:EasySkinSwitch:1.0.0'
    }


### 第三步：开始使用，步骤如下

#### （1）初始化，在Application的onCreate方法中执行以下代码
        SkinManager.init(this)
#### （2）针对支持库或自定义view（简单理解就是在布局文件里的这种 <xxx.xxx.xxxView）需要去实现SkinViewSupportInter接口，并实现其applySkin方法，示例如下：
        
        /**
        * 自定义view以实现换肤功能
        */
        class SkinFloatActionView : FloatingActionButton, SkinViewSupportInter {
          var bgTintColorId: Int = 0
          constructor(context: Context) : this(context, null) {
          }
        
          constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {
        
          }
        
          constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
              context,
              attrs,
              defStyleAttr
          ) {
              val obtainStyledAttributes =
              context.obtainStyledAttributes(attrs, R.styleable.FloatingActionButton, defStyleAttr, 0)
              bgTintColorId =
              obtainStyledAttributes.getResourceId(R.styleable.FloatingActionButton_backgroundTint, 0)
          }
        
          override fun applySkin() {
              if (bgTintColorId != 0) {
                  backgroundTintList = ResourcesManager.getColorStateList(bgTintColorId)
              }
          }
        }
        
#### （3）对主题颜色进行单独换肤处理
        状态栏的颜色单独定义到colors.xml文件中，换肤时，单独调用一下状态栏的颜色设置即可
        建议在项目的BaseActivity的onCreate方法中调用库中的SkinThemeUtils.updateStatusBarColor方法如下：
        （R.color.status_bar 需要开发者自定义创建）

        /**
          * 基类Activity 统一设置主题啥的
          */
          open class BaseActivity : AppCompatActivity() {
              override fun onCreate(savedInstanceState: Bundle?) {
                  super.onCreate(savedInstanceState)
                  SkinThemeUtils.updateStatusBarColor(this, R.color.status_bar)
              }
          }

#### （4）代码中动态设置颜色、背景等皮肤相关的地方要单独进行处理
        例如，app工程中我们主页底部tab的图标和颜色都是根据用户点击来动态设置的，这里可以这样处理
        a.设置Tab的颜色和图标时用使用库中封装好的ResourcesManager来获取资源id对应的值
            ResourcesManager.getColor(xxx)
            ResourcesManager.getDrawable(xxx)
        b.当前所在的Activity实现SkinViewSupportInter接口，实现接口中的applySkin方法，当触发换肤时,回进行回调
        请参考app工程中MainActivity的实现方式
        ![](https://github.com/ZS-ZhangsShun/EasySkinSwitch/blob/master/app/img/ResourcesManager.png)
        ![](https://github.com/ZS-ZhangsShun/EasySkinSwitch/blob/master/app/img/MainActivity.png)
        ![](https://github.com/ZS-ZhangsShun/EasySkinSwitch/blob/master/app/img/MainActivity_applySkin.png)
#### （5）需要换肤时调用SkinManager.loadSkin(皮肤包绝对路径)来换肤，如app工程NewsFragment所示：
        //换肤
        SkinManager.loadSkin(EasyVariable.mContext.cacheDir.absolutePath
            + File.separator + "skinonly.apk")
        }
        //恢复默认皮肤
        SkinManager.loadSkin(null)

## 混淆配置
-keep com.zs.skinswitch.** {*;}
