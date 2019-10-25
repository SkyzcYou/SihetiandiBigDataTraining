# 手机数据分析实训三

难度等级：中等



## 一、项目简介

通过上一个任务，我们已经对大数据应用项目的工作流程中涉及到的基本技术技能进行了训练，通过上一个实训任务，我们向厂商提出了关于手机销售方面的建议。本章我们尝试对于手机的配置进行一次市场调研，比如哪些手机CPU占比较高，手机RAM多大算是主流配置，手机ROM和销量的关系以及手机操作系统市场占比情况。

### 1.1 知识点

本实训内容涉及到以下知识点：

- 数据采集
  - 使用工具（Chrome开发者工具）查看网页源码，分析网页结构，明确数据采集对象
  - 网络请求
  - 数据分析及提取
  - 本地目录操作、文件创建、读写
- 数据清洗
  - HDFS数据文件读取、解析、清洗过滤，分区
  - MapReduce程序的编译、打包、发布
  - 执行MapReduce程序，完成数据清洗
- 数据分析
  - Hive建库、建表
  - Hive数据加载
  - HQL编写、数据查询统计
- 数据分析及数据可视化
  - MySQL数据处理
  - 前端数据展示
- 数据分析报告编写

### 1.2 前期准备工作

为了能够顺利的完成本次实训任务，你至少需要了解以下基础知识：

- Python程序开发基础
- Java程序开发基础
- 了解基于Hadoop的离线大数据分析平台的运行机制
- MySQL数据库的远程连接、数据访问

## 二、数据集简介

本次训练所使用的数据集为某大型电子商务网站采集的手机数据，文件名称为jd.json，该数据包含了一部分数据缺失或不准确的脏数据，数据集文件中每一行数据为一个用户对其购买的手机的评论数据，数据格式为json，数据组织格式如下：

```
Data1
Data2
Data3
...
DataN
```

一条数据内容格式如下：

```
{
  'phone_brand': '小米（MI）',		// 手机品牌名称
  'phone_name': '小米红米6 Pro',	// 手机型号
  'parameter': [
    {
      '品牌': '小米（MI）',
      '型号': '红米6 Pro',
      '入网型号': '以官网信息为准',
      '上市年份': '2018年',
      '上市月份': '6月'
    },
    {
      '机身颜色': '曜石黑',
      '机身长度（mm）': '149.33',
      '机身宽度（mm）': '71.68',
      '机身厚度（mm）': '8.75',
      '机身重量（g）': '178',
      '运营商标志或内容': '无',
      '机身材质分类': '其他'
    },
    {
      '操作系统': 'Android'
    },
    {
      'CPU品牌': '骁龙（Snapdragon)',
      'CPU频率': '以官网信息为准',
      'CPU核数': '八核',
      'CPU型号': '骁龙625（MSM8953）'
    },
    {
      '双卡机类型': '双卡双待单通',
      '最大支持SIM卡数量': '2个',
      'SIM卡类型': 'Nano SIM',
      '4G网络': '4G：移动（TD-LTE)；4G：联通(FDD-LTE)；4G：电信(FDD-LTE)；4G：联通(TD-LTE)',
      '3G/2G网络': '3G：移动(TD-SCDMA)；3G：联通(WCDMA)；3G：电信(CDMA2000)；2G：移动（GSM）+联通(GSM)；2G：电信(CDMA)；2G：移动联通(GSM)+电信(CDMA)',
      '副SIM卡4G网络': '不支持主副卡同时使用电信卡',
      '网络频率（2G/3G）': '2G：GSM 850/900/1800/1900；2G：CDMA 800；3G：TD-SCDMA 1900/2000；3G：WCDMA 850/900/1900/2100；3G：CDMA2000；2G：GSM 900/1800；2G：GSM 900/1800/1900；3G：CDMA 800MHz 1X&EVDO；3G：WCDMA：850/900/1700/1900/2100MHz',
      '是否支持同时使用联通卡': '不可同时上网，仅支持卡A上网，卡B通话。'
    },
    {
      'ROM': '32GB',
      'RAM': '3GB',
      '存储卡': '支持MicroSD（TF）',
      '最大存储扩展容量': '256GB'
    },
    {
      '主屏幕尺寸（英寸）': '5.84英寸',
      '分辨率': '2280x1080',
      '屏幕像素密度（ppi）': '432',
      '屏幕材质类型': '其他'
    },
    {
      '前置摄像头': '500万像素',
      '前摄光圈大小': '其他',
      '美颜技术': '支持',
      '拍照特点': '美颜'
    },
    {
      '摄像头数量': '2个',
      '后置摄像头': '1200+500万像素',
      '摄像头光圈大小': 'f/2.2',
      '拍照特点': '美颜；连拍；全景；夜间拍摄'
    },
    {
      '电池容量（mAh）': '4000mAh（typ）/3900mAh（min）',
      '电池是否可拆卸': '否',
      '充电器': '5V/2A'
    },
    {
      '数据传输接口': '蓝牙',
      'NFC/NFC模式': '不支持',
      '耳机接口类型': '3.5mm',
      '充电接口类型': 'Micro USB'
    },
    {
      '指纹识别': '支持',
      'GPS': '支持',
      '陀螺仪': '支持',
      '其他': '距离感应；光线感应'
    },
    {
      '常用功能': '录音；便签；超大字体'
    }
  ],
  'comments': {
    'comment_user': '爱***包',				// 评论用户
    'appraise': '很棒哦，超级喜欢啊...',		   // 用户评论内容
    'buy_color': '曜石黑',					   // 用户购买的手机他颜色
    'buy_size': '3GB+32GB',			 		 // 用户购买的手机型号
    'buy_date': '2018-09-17 15:23:44',		 // 用户购买时间
    'buy_client': '来自京东iPhone客户端',		// 用户购买渠道
    'score': 5,								 // 用户评分
    'upvote': 1								 // 用户评论点赞数量
  }
}
```



# 三、运行环境

| 名称       | 硬件      | 软件                     | IP地址        | 用途                                                  |
| ---------- | :-------- | ------------------------ | ------------- | ----------------------------------------------------- |
| Master     | 内存：16G | CentOS7，Hadoop组件      | 192.168.3.100 | 离线分析系统集群主节点                                |
| Slave1     | 内存：8G  | CentOS7，Hadoop组件      | 192.168.3.101 | 离线分析系统集群从节点                                |
| Slave2     | 内存：8G  | CentOS7，Hadoop组件      | 192.168.3.102 | 离线分析系统集群从节点                                |
| DS-Dev     | 内存：4G  | Ubuntu 16.04LTS，PyCharm | 192.168.3.120 | 用于完成数据采集相关开发、调试工作的客户端环境        |
| DA-Dev     | 内存：4G  | Ubuntu 16.04LTS，IDEA    | 192.168.3.121 | 用于完成MapReduce相关开发、调试、发布工作的客户端环境 |
| DV-Dev     | 内存：4G  | Ubuntu 16.04LTS，PyCharm | 192.168.3.122 | 用于完成数据可视化相关开发、调试工作的客户端环境      |
| Web-Server | 内存：4G  | CentOS                   | 192.168.3.124 | 模拟电子商务网站，作为数据采集对象                    |



## 四、实训任务

从本章开始，我们将逐步完成一个大数据离线分析项目的全部工作过程，请按照指定的任务步骤完成所有任务，全部实训任务需要在**4小时内**完成，祝各位好运。



### 任务一、数据采集

根据需求，需要对目标电子商务网站的数据进行采集，目标电子商务网站网址为`http://192.168.3.124:28080/LexianMall/sc/index.html`，请使用Chrome浏览器提供的开发者工具分析网页源代码，完成数据采集代码的编写，运行数据采集程序，完成数据采集工作。

#### 1. 使用Chrome浏览器打开网页，确定以下内容：

- 乐鲜生活首页商品分类列表

#### 2. 使用PyCharm打开`Pr_Task3_1`项目，完成`crawl.py`文件的以下函数实现：

- **def response_handler(self, url)**

  - 功能

    使用目标网页URL构造Response对象

  - 参数

    url,目标网页的URL,数据类型:String

  - 返回值

    response ,Response对象,数据类型:Response Object

- **def parse_Categories(self, response)**

  - 功能

    解析分类列表的解析函数，得到所有分类及分类ID

  - 参数

    response，说明：使用分类列表API所构造的相应对象，数据类型：Response Object

  - 返回值

    categories， 说明：分类及分类ID的Dict，数据类型：Dict,数据格式：{category1:id1,category2:id2,categoryN:idN}

- **parse_SubCategories(self, response)**

  - 功能

    解析该分类下子分类列表的解析函数，得到该分类下所有分类及分类ID

  - 参数

    response，说明：使用子分类列表API所构造的相应对象，数据类型：Response Object

  - 返回值

    sub_categories， 说明：子分类及子分类ID的Dict，数据类型：Dict,数据格式：{sub_category1:[category_name1,category_name2,category_nameN]}

- **def save_data(self, item)**

  - 功能

    将数据存储到项目目录文件下的`Pr_Task3_1-{YYYY-MM-DD}-crawl_data.json`文件当中去，其中文件的创建在该类的初始化呢函数内使用datetime模块生成

  - 参数

    item,单条数据由items迭代产生,数据类型:Dict

  - 返回值

    无

- **def crawl(self,category_id)**

  - 功能

    单一主分类下所有子分类的采集函数，由main函数调用

  - 参数

    category_id,主分类的ID ,数据类型Int

  - 返回值

    同函数parse_SubCategories返回值

### 任务二、数据清洗

从目标电子网站采集到的数据保存在HDFS的`/Initial_Data/`目录下，根据项目需求，需要对采集到的数据进行不合规数据的清洗工作。请完成MapReduce程序的代码编写、程序打包发布并在服务器运行完成数据清洗工作。

#### 1.  使用IDEA打开`Pr_Task3_3`项目，完成`\src\main\java\Phone_MR\Phone_Map_3.java`文件的以下函数的编码实现：

- **PrePprocessData(String value)**

  - 功能

    处理一条数据中的特殊字符，清洗规则如下：

    - 将字符`|`替换为`-`
    - 将特殊字符串`\r\t`删除

  - 参数

    | 参数名 | 数据类型 | 说明     |
    | ------ | -------- | -------- |
    | value  | String   | 原始数据 |

  - 返回值

    | 返回值名 | 数据类型 | 说明           |
    | -------- | -------- | -------------- |
    | -        | String   | 处理完成的数据 |

- **GetStrigByName(String rawJson String kyesName)**

  - 功能

    解析JSON格式数据，根据数如参数从JSON中提取目标字符串

  - 参数

    | 参数名   | 数据类型 | 说明               |
    | -------- | -------- | ------------------ |
    | rawJson  | String   | 原始JSON格式字符串 |
    | keysname | String   | 获取字段的key      |

  - 返回值

    | 返回值名 | 数据类型 | 说明                                                         |
    | -------- | -------- | ------------------------------------------------------------ |
    | -        | String   | 提取到的字符串，若Key存在，则返回数据，若Key不存在则返回空字符串 |



- **GetPhoneParameter(String rawJson,String KeyName)**

  - 功能

    将传入参数整理为以下格式：

  - 参数

    | 参数名   | 数据类型 | 说明               |
    | -------- | -------- | ------------------ |
    | rawJson  | String   | 原始JSON格式字符串 |
    | keysname | String   | 获取字段的key      |

  - 返回值

    | 返回值名 | 数据类型 | 说明                |
    | -------- | -------- | ------------------- |
    | rawValue | String   | 索要获取字段的value |

  

- **FilterData(String [] parparamArr)**

  - 功能

    过滤不合规的数据

  - 参数

    | 参数名      | 数据类型 | 说明                     |
    | ----------- | -------- | ------------------------ |
    | parparanArr | String[] | 需要进行过滤的字段的数组 |

  - 返回值

    | 返回值名    | 数据类型 | 说明            |
    | ----------- | -------- | --------------- |
    | false、true | boolean  | 返回false和true |

  

#### 2. 使用IDEA完成MapReduce程序的打包编译工作，生成程序包，程序包名称为`Pr_Task3_3.jar`

#### 3. 请将打包后的清洗程序包`Pr_Task3_3.jar`上传到Master主机的`/home`目录下，使用shell命令运行程序，完成数据清洗。



### 任务四、数据分析

大数据分析平台通过运行MapReduce程序已成功将不合规数据清洗完成，根据项目需求，需要将清洗后的数据加载到Hive数据仓库中进行数据分析工作。请完成以下各步骤工作：

#### 1. 按照以下要求创建数据仓库及Hive表

- 数据仓库名称：db_phone_raw_3

- 表名称：tbl_phone_data_3

  - 原始数据表

    - 表名称：tbl_phone_data_3

    - 结构：内部表

      | 列名          | 数据类型 |   说明   |
      | ------------- | -------- | :------: |
      | fld_phone_os  | String   | 操作系统 |
      | fld_phone_cpu | String   |   cpu    |
      | fld_phone_ram | string   |   ram    |
      | fld_phone_rom | string   |   rom    |

- 手机RAM销售量统计表

  - 表名称tbl_sales_ram_count

  - 结构：内部表

    | 字段名         | 属性   | 说明   |
    | -------------- | ------ | ------ |
    | fld_ram_name   | string | ram    |
    | fld_sale_count | string | 销售量 |

- 手机操作系统销售量统计表

  - 表名称：tbl_sales_os_count

  - 结构：内部表

    | 字段名         | 属性   | 说明     |
    | -------------- | ------ | -------- |
    | fld_os_name    | string | 操作系统 |
    | fld_sale_count | int    | 销售量   |

- 手机CPU型号销售量统计表

  - 表名称：tbl_sales_cpu_count

  - 结构：内部表

    | 字段名         | 属性   | 说明    |
    | -------------- | ------ | ------- |
    | fld_cpu_name   | string | cpu型号 |
    | fld_sale_count | int    | 销售量  |

- 手机ROM销售量统计表

  - 表名称：tbl_sales_rom_count

  - 结构：内部表

    | 字段名         | 属性   | 说明   |
    | -------------- | ------ | ------ |
    | fld_rom_name   | string | rom    |
    | fld_sale_count | string | 销售量 |

#### 2. 将HDFS的`/data/`下的全部数据加载到Hive表`tbl_phone_data_3中

1. 下载附件中的数据文件，通过SSH命令上传到Slave2服务器
2. 使用HDFS命令创建目录`/data/`
3. 使用HDFS命令将文件上传到HDFS的`/data/`目录下
4. 使用Hive命令将数据加载到指定表中

#### 3. 运行HQL命令，统计以下数据

- 各CPU型号的数据量
- 各RAM的数据量
- 各ROAM的数据量
- 各操作系统的数据量

### 任务五、数据可视化

分析后的数据已推送到MySQL数据库中，MySQL数据库结构如下所示：

数据库名称：db_phone_analysis_data_3

- **表1：手机CPU型号销售量统计表**

  - 表名：tbl_sales_cpu_count_pro

  - 表结构：

    | 字段名         | 属性               | 主键 | 外键 | 说明     |
    | -------------- | ------------------ | ---- | ---- | -------- |
    | fld_index      | int，非空          | 是   | 否   | 自增字段 |
    | fld_cpu_name   | varchar(256)，非空 | 否   | 否   | cpu值    |
    | fld_sale_count | int，非空          | 否   | 否   | 销售量   |

- **表2：手机RAM销售量统计表**

  - 表名：tbl_sales_ram_count_pro

  - 表结构：

    | 字段名         | 属性               | 主键 | 外键 | 说明     |
    | -------------- | ------------------ | ---- | ---- | -------- |
    | fld_index      | int，非空          | 是   | 否   | 自增字段 |
    | fld_ram_name   | varchar(256)，非空 | 否   | 否   | ram值    |
    | fld_sale_count | int，非空          | 否   | 否   | 销售量   |

- **表3：手机ROM销售量统计表**

  - 表名：tbl_sales_rom_count_pro

  - 表结构：

    | 字段名         | 属性               | 主键 | 外键 | 说明     |
    | -------------- | ------------------ | ---- | ---- | -------- |
    | fld_index      | int，非空          | 是   | 否   | 自增字段 |
    | fld_rom_name   | varchar(256)，非空 | 否   | 否   | rom值    |
    | fld_sale_count | int，非空          | 否   | 否   | 销售量   |

- **表4：手机操作系统销售量统计表**

  - 表名：tbl_sales_os_count_pro

  - 表结构：

    | 字段名         | 属性               | 主键 | 外键 | 说明         |
    | -------------- | ------------------ | ---- | ---- | ------------ |
    | fld_index      | int，非空          | 是   | 否   | 自增字段     |
    | fld_os_name    | varchar(256)，非空 | 否   | 否   | 操作系统名称 |
    | fld_sale_count | int，非空          | 否   | 否   | 销售量       |

#### 1. 使用pycharm打开项目`Pr_task3_5`,完成以下文件的类定义：

- **DevelopmentConfig()、TestingConfig()、ProductionConfig()**

  - 功能：在`./app/config.py`文件中依次完成以上三个环境下的mysql配置类，三个类中具体内容完全一致。

  - 参数：本地mysql环境中用户名、密码、IP地址、端口号、数据库名称参数如下

    ```
    mysql:{
        user : root,    				    //用户名称
        password : 123456,				    //用户密码
        ip : 192.168.3.122,				    //IP地址
        port : 3306,					   //端口号
        db_name : db_phone_analysis_data_3   //数据库名称
    }
    ```

  - 返回值：无 

- **在shell命令行终端中打开mysql，并实现数据库表之间的数据传输**

  数据库db_phone_analysis_data_3中存在有以下四个表：

  | 表名称              | 字段名称              | 字段名称            |
  | ------------------- | --------------------- | ------------------- |
  | tbl_sales_cpu_count | fld_cpu_name(varchar) | fld_sale_count(int) |
  | tbl_sales_ram_count | fld_ram_name(varchar) | fld_sale_count(int) |
  | tbl_sales_rom_count | fld_rom_name(varchar) | fld_sale_count(int) |
  | tbl_sales_os_coun   | fld_os_name(varchar)  | fld_sale_count(int) |

    将以上四个表中的数据用sql命令依次导入到数据库中相对应的另外四个表中，如下：

  | 表名称                  | 字段名称(自增) | 字段名称              | 字段名称            |
  | ----------------------- | -------------- | --------------------- | ------------------- |
  | tbl_sales_cpu_count_stu | fld_index(int) | fld_cpu_name(varchar) | fld_sale_count(int) |
  | tbl_sales_ram_count_stu | fld_index(int) | fld_ram_name(varchar) | fld_sale_count(int) |
  | tbl_sales_rom_count_stu | fld_index(int) | fld_rom_name(varchar) | fld_sale_count(int) |
  | tbl_sales_os_count_stu  | fld_index(int) | fld_os_name(varchar)  | fld_sale_count(int) |

#### 2.  完成`./app/templates/main/index.html`文件的以下函数编码实现：

- **drawOsChart()**

  - 功能：仿照页面中drawCpuChart()方法，编写drawOsChart()方，图表配置与drawCpuChart()中一致为饼状图，其中需要注意的配置项有以下内容：

    ```
    ID : osChart   					//drawOsChart()方法中所需页面元素ID
    dataArr : tsoc_res				//后台传输到前台页面中的数据模板，数组
    name_field : fld_os_name		//数据模板中的系统名称字段，字符串
    value_field : fld_sale_count	//数据模板中的销售量字段，整数值
    ```

    - 参数：无
    - 返回值：无

### 3. 在Pycharm中运行后台程序，生成网站地址，使用Chrome浏览器访问并查看显示效果



## 任务五、编写分析报告

根据数据可视化显示内容，按照分析报告模板要求完成数据分析报告。请结合数据分析结果回答以下问题：

- 问题一：哪些CPU销售占比较高？
- 问题二：什么手机RAM配置属于主流？
- 问题三：手机ROM大小和销售量之间的关系如何？
- 问题四：手机操作系统的市场占比情况如何？

根据上述分析，为某厂家即将生产的手机提供配置建议。