## trpg-java-dice

**简介：**
- 该项目主要负责TRPG中的骰子逻辑处理。
- 该模块主要分为 指令识别 、 数值运算。
- 通过在框架上设计的时候，应当从不依赖其他项目的基础上去实现。从而极大的降低耦合度
- 指令的识别通过的是注解反射的形式执行。
- 该项目为 [mirai-rulateday-dice](https://github.com/Eiriksgata/mirai-rulateday-dice) 的依赖项目。
- 该骰子的开发设定主要是目前两大跑团体系：DND 、 COC。
- 目前作者开发的定义版本为 DND5e 、 COC7版。
- 该项目将会和 `mirai-rulateday-dice`同步更新。
- 如果你有什么疑问或者讨论方案，可以选择在在[discussions](https://github.com/Eiriksgata/mirai-rulateday-dice/discussions)进行

## 快速使用

**Maven**

Step 1. Add the JitPack repository to your build file
```XML
<repositories>
	<repository>
		   <id>jitpack.io</id>
		   <url>https://www.jitpack.io</url>
	</repository>
</repositories>
```
Step 2. Add the dependency
```XML
<dependency>
	 <groupId>com.github.Eiriksgata</groupId>
	 <artifactId>trpg-java-dice</artifactId>
	 <version>TAG</version> <!-- 请使用最新版 -->
</dependency>
```
- 引入方式参考:[jitpack/Eiriksgata/trpg-java-dice](https://www.jitpack.io/#Eiriksgata/trpg-java-dice/1.1.2-alpha)

- 引用后需要在当前项目的Resources 资源路径下新建一个配置文件，该文件用于配置反射扫描包路径，具体参考**配置文件说明**

**配置文件说明**
- 如果引用该依赖，需要在资源项目中加入以下配置文件（例如Spring架构或者mybatis那样）
配置文件名

**trpg-dice-config.properties**

```properties
## trpg-java-dice Config
reflections.scan.path=indi.eiriksgata
```

indi.eiriksgata 为你在项目中使用的@InstructService | @InstructReflex 类的包路径（类似于Mybatis Mapper 的包扫描，此操作仅是加快处理反应速度。越具体越快速）

具体使用请参考 [Mirai-Rualteday-Dice](https://github.com/Eiriksgata/mirai-rulateday-dice) 项目


**开发模块划分**
* 指令识别
* 骰子运算核心
* 自定义文本回复

## 指令识别

- 用户可以通过指令来使骰子程序生成不同的数值。
- 除此之外还可以更改骰子的默认属性或者是操作菜单等。
- 目前是做了一个大概的开发代码的思路模板。
- 本项目中不参与指令的形式设置，只参与参数的传入，进行处理后返还数据结果。

**注解：**
* `@InstructService` 注解为标记指令模块类，用于反射架构扫描。
* `@InstructRelflex` 主要标记为指令识别类，当消息传递过来后，将会运行有该指令标记的方法。使用请参考代码中的例子。
参数说明：priority 为 指令优先级，如果出现拥有相同关键字的文本，数值越大的会越先执行。`例如: .rh > .r`
* (关于注解扫描的包的位置，后续会像Spring的设计，增加一个注解扫描包的配置文件，用于开发者填写使用)

**反射实现处理类**
- `InstructHandle` 利用`reflections` 反射框架来实现的检测。
- 指令模块通过注解反射的形式，减少了代码实现的耦合度。
- 开发人员可以通过在集成的项目中使用注解，从而进行更多的额外操作。
- 这里只是提供了指令的反射处理，并没有做指令的具体实现。具体实现请参考 `mirai-rulateday-dice` 的实现。
- 特殊的指令如果需要后续的修改项，应当考虑使用回调的形式进行。

## 骰子运算

- 骰子运算的基础雷包含在 `RollBasicsImpl` 

- 因为跑团中基本使用到属性检测的，基本只有coc 而目前大部分的版本为 coc7版
在7版中，大部分以100面骰子。
本项目中也提供了大量的骰子默认配置项，用于更改。

- 在该模块中包含了 骰子数字生成、属性检测、理智检测等


## 自定义文本回复

骰子计算后生成的文本会通过该静态方法处理后返回文本。



## 配置文件说明
**在该项目中包含了多个配置文件，其中涉及骰子的默认设置，以及自定义返回文本的配置项、国际化文件等**
* **custom-text.properties** 自定义返回文本配置文件，用户可以根据默认的文本内容进行参考。系统的返回文本会优先检测该配置文件的词条，如果没有则按默认的文本配置文件返回。
* **default-text.properties** 默认的文本返回配置文件，如果用户没有使用自定义文本，则使用该配置文件。
* **dice-config.properties** 骰子系统的默认数值配置文件，其中包含了骰子默认类型、骰子个数最大最小值、骰子面数最大最小值、骰子系统的默认语言、coc7规则界限等。
* **rulebook.coc.properties** coc规则书的文本内容（暂时保留）
* **rulebook-dnd.properties** dnd规则书文本内容（暂时保留）



