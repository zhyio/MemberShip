# MemberShip - 相亲会员管理系统

一款现代化的 Android 相亲会员管理应用，帮助婚恋从业者高效管理会员信息、生成精美相亲海报。

## 技术栈

| 类别 | 技术 |
|------|------|
| 语言 | Kotlin 1.9.22 |
| UI | Jetpack Compose + Material3 |
| 架构 | MVVM (ViewModel + StateFlow) |
| 依赖注入 | Hilt 2.50 |
| 本地数据库 | Room 2.6.1 |
| 图片加载 | Coil 2.5.0 |
| 导航 | Navigation Compose 2.7.7 |
| 动画 | Compose Animation |
| 构建 | Gradle 8.5 + AGP 8.2.2 |

## 功能模块

### 首页 - 会员列表

- **统计卡片**：总会员数、男女比例，数字递增动画
- **搜索功能**：按姓名、城市、学历、职业模糊搜索，300ms 防抖
- **筛选功能**：全部 / 男士 / 女士 快捷筛选标签
- **排序功能**：按更新时间、创建时间、姓名、年龄四种排序
- **网格布局**：两列瀑布流网格，渐变头像 + 姓名 + 年龄 + 城市 + 婚姻标签
- **收藏功能**：卡片右上角心形按钮，支持收藏 / 取消收藏
- **入场动画**：列表项逐项 fadeIn + slideUp + 缩放，带 stagger 延迟
- **按压交互**：卡片按压缩放 0.95x，弹性回弹
- **空状态**：无数据时显示引导提示

### 会员详情页

- **照片轮播**：顶部照片区域支持左右滑动
- **照片浏览器**：点击照片进入全屏查看，HorizontalPager + 页码指示器
- **信息卡片**：基本信息、婚姻情感、家庭背景、择偶要求四大模块，双列排版
- **FAB 菜单**：底部浮动按钮展开编辑 / 海报两个子按钮，带文字标签和旋转动画
- **删除确认**：删除前弹出确认对话框

### 添加 / 编辑会员

- **步骤指示器**：三步流程（基本信息 → 婚姻家庭 → 择偶要求），圆点 + 连接线 + 完成打勾动画
- **步骤切换**：横向滑入 / 滑出 + 淡入淡出
- **基本信息**：照片选择器（最多 6 张）、姓名、性别、出生日期、身高体重、学历、职业、收入、城市、籍贯
- **婚姻家庭**：婚姻状况、子女情况、情感经历、父母情况、兄弟姐妹、家庭经济状况
- **择偶要求**：年龄范围、身高范围、期望学历、接受婚姻状况、期望城市、其他要求

### 相亲海报生成

- **三种模板**：
  - **玫瑰渐变**：暖色渐变背景，圆形头像，白色信息卡片
  - **极简黑白**：深灰渐变背景，几何线条装饰，白色卡片
  - **梦幻紫蓝**：紫色渐变背景，星星装饰，光晕头像效果
- **半嵌入式设计**：大尺寸头像压在信息卡片上方，带阴影和描边
- **双列信息排版**：基础信息和择偶要求均采用双列布局，标签灰色 + 内容深色
- **空字段隐藏**：信息为空时不显示在海报上
- **操作功能**：保存到相册、分享

## 项目结构

```
app/src/main/java/com/membership/app/
├── MembershipApplication.kt          # Application 入口
├── MainActivity.kt                    # 主 Activity
├── domain/model/
│   └── Member.kt                     # 领域模型 + PartnerRequirements
├── data/
│   ├── local/
│   │   ├── MembershipDatabase.kt     # Room 数据库
│   │   ├── entity/MemberEntity.kt    # 数据库实体
│   │   └── dao/MemberDao.kt          # 数据访问对象
│   ├── repository/MemberRepository.kt # 仓库层
│   └── di/DatabaseModule.kt          # Hilt 依赖注入 + 种子数据
├── ui/
│   ├── theme/                        # Aurora 主题系统
│   │   ├── Color.kt                  # 色彩定义
│   │   ├── Theme.kt                  # Material3 主题
│   │   ├── Type.kt                   # 字体排版
│   │   └── Shapes.kt                 # 形状定义
│   ├── components/                   # 通用组件
│   │   ├── ModernEffects.kt          # Aurora 背景 + 毛玻璃面板 + 光晕
│   │   ├── Animations.kt             # 入场动画 + 按压缩放 + 计数动画
│   │   ├── StatCard.kt               # 统计卡片
│   │   ├── Stepper.kt                # 步骤指示器
│   │   ├── PhotoPicker.kt            # 照片选择器
│   │   └── ShimmerEffect.kt          # 骨架屏
│   ├── navigation/MembershipNavHost.kt # 导航路由
│   └── screens/
│       ├── home/                     # 首页
│       ├── detail/                   # 会员详情
│       ├── edit/                     # 添加/编辑
│       └── poster/                   # 海报预览
└── utils/
    ├── PosterGenerator.kt            # 海报 Canvas 绘制
    └── AvatarGenerator.kt            # 种子数据头像生成
```

## 设计风格

- **Aurora 深色主题**：InkBlack 背景 + 玫瑰粉 / 青色 / 暖金三色点缀
- **毛玻璃面板**：GlassPanel 组件，半透明背景 + 渐变边框
- **光晕效果**：GlowOrb 模糊光球装饰
- **动效克制**：静态背景 + 一次性计数动画，无持续晃动

## 构建与运行

```bash
# 克隆项目
git clone https://github.com/zhyio/MemberShip.git
cd MemberShip

# 构建 Debug APK
./gradlew assembleDebug

# 安装到设备
adb install app/build/outputs/apk/debug/app-debug.apk
```

**环境要求**：
- Android SDK (compileSdk 34)
- JDK 17
- Gradle 8.5

## 应用截图

应用首次启动会自动加载 10 条带渐变头像的示例数据，方便体验所有功能。

## License

MIT
