# ClassLoader

ClassLoader是一个抽象类,其中定义了ClassLoader的主要功能。
BootClassLoader是ClassLoader的内部类,用于预加载常用类,比如加载一些系统Framework层级需要的类,Android应用里也需要用到一些系统的类等,是Android平台上所有ClassLoader的最终parent,这个内部类是包内可见,所以没法使用。

SecureClassLoader类和JDK8中的SecureClassLoader类的代码是一样的,它继承了抽象类ClassLoader。SecureClassLoader并不是ClassLoader的实现类,而是拓展了ClassLoader类加入了权限方面的功能,加强了ClassLoader的安全性。

BaseDexClassLoader继承自ClassLoader,是抽象类ClassLoader的具体实现类,PathClassLoader和DexClassLoader都继承它。

PathClassLoader加载系统类和应用程序的类,如果是加载非系统应用程序类,则会加载data/app/目录下的dex文件以及包含dex的apk文件或jar文件
DexClassLoader 可以加载自定义的dex文件以及包含dex的apk文件或jar文件,也支持从SD卡进行加载
InMemoryDexClassLoader是Android8.0新增的类加载器,继承自BaseDexClassLoader,用于加载内存中的dex文件。

例如:
Activity的类加载器是BootClassLoader
MainActivity,AppcompatActivity类的加载器是PathClassLoader

## 加载过程

对于任意一个类,都需要由它的类加载器和这个类本身一同确定其在就Java虚拟机中的唯一性,即使两个类来源于同一个Class文件,只要加载它们的类加载器不同,那这两个类就必定不相等。
这里的"相等"包括了代表类的Class对象的equals()、isAssignableFrom()、isInstance()等方法的返回结果,也包括了使用instanceof关键字对对象所属关系的判定结果。

如果一个类加载器收到了加载类的请求,它不会自己立即去加载类,它会先去请求父类加载器,每个层次的类加载器都是如此。层层传递,直到传递到最高层的类加载器,只有当父类加载器反馈自己无法加载这个类,才会有当前子类加载器去加载该类。
