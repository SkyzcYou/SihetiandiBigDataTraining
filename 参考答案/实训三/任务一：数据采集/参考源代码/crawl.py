import os
import time
import json
import datetime
import requests


class Crawl(object):
    '''
    商品数据采集的类
    '''
    def __init__(self):
        '''
        说明：类初始化操作
        '''
        self.headers = {
            'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
            'Accept-Encoding': 'gzip, deflate',
            'Accept-Language': 'zh-CN,zh;q=0.9',
            'User-Agent': 'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36'
            }
        file_dir = os.getcwd()
        file_name = 'Pr_Task3_1-%s-crawl_data.json'%str(datetime.datetime.now().date())
        self.file_path = os.path.join(file_dir,file_name)
        self.fp = open(self.file_path,'w',encoding='utf-8')
        self.fp.write('[\n')
        print('初始化成功')


    def response_handler(self,url,data):
        '''
        说明：使用目标网页Url或接口构造Response对象
        参数：url,目标网页的Url或接口的API ,数据类型String
        返回值：response,Response对象,数据类型Response Object
        '''
        print("开始构造响应")
        response = requests.post(url=url,data=data,headers=self.headers)
        print('构造成功')
        return response

    def parse_Categories(self, response):
        '''
        说明：解析分类列表的解析函数，得到所有分类及分类ID
        参数：response，说明：使用分类列表API所构造的相应对象，数据类型：Response Object
        返回值: categories， 说明：分类及分类ID的Dict，数据类型：Dict,数据格式：{category1:id1,category2:id2,categoryN:idN}
        '''
        print('正在解析主分类列表')
        categories = {data.get('categoryName'):data.get('categoryId') for data in json.loads(response.text).get('data')}
        print('解析得到主分类:',list(categories.keys()))
        return categories

    def parse_SubCategories(self, response):
        '''
        说明：解析该分类下子分类列表的解析函数，得到该分类下所有分类及分类ID
        参数：response，说明：使用子分类列表API所构造的相应对象，数据类型：Response Object
        返回值: sub_categories， 说明：子分类及子分类ID的Dict，数据类型：Dict,数据格式：{sub_category1:[category_name1,category_name2,category_nameN]}
        '''
        sub_categories = {data.get('categoryName'):[sub_category['categoryName'] for sub_category  in data.get('subCategories')]if data.get('subCategories') else None  for data in json.loads(response.text).get('data') if json.loads(response.text).get('data')}
        return sub_categories



    def save_data(self,item):
        '''`
        说明：将解析到的数据存储到指定目录下的Json文件中去
        参数：item,一条数据,数据类型Dict
        返回值：无
        '''

        data = json.dumps(item,ensure_ascii=False)
        print('保存数据%s'%data)
        self.fp.write(data+'\n')

    def crawl(self,category_id):
        '''
        说明：单一主分类下所有子分类的采集函数
        参数 :category_id,主分类的ID ,数据类型Int
        返回值：同函数parse_SubCategories返回值
        '''
        sub_categorie_url = 'http://192.168.3.124:28080/LexianMall/category/getSubCategories.do'
        sub_categories_data = {
            'parentId': category_id,
            'os': 'pc',
            'platfornCode': 2,
            'unixtime': int(time.time())
        }
        sub_categorie_res = self.response_handler(sub_categorie_url, sub_categories_data)
        sub_categories = self.parse_SubCategories(sub_categorie_res)
        print('解析到子分类：',sub_categories)
        return sub_categories


    def close_spider(self):
        '''
        说明：爬虫关闭爬虫时的一些操作
        参数：无
        返回值：无
        '''
        self.fp.write(']')
        self.fp.close()
        print('采集完成!数据存储在%s'%self.file_path)


    def main(self):
        '''
        说明：主函数，处理逻辑编写的地方
        参数：无
        返回值：无
        '''
        categories_url = 'http://192.168.3.124:28080/LexianMall/category/getCategories.do'
        print('开始进行采集，目标网址URL或接口为：%s' % categories_url)
        categories_data = {
                'type':1,
                'os':'pc',
                'platfornCode':2,
                'unixtime':int(time.time())
            }
        categories_res = self.response_handler(categories_url,categories_data)
        categories = self.parse_Categories(categories_res)
        item = {category:self.crawl(categories[category])  for category in categories.keys()}
        self.save_data(item)
        self.close_spider()




if __name__ == '__main__':
    c=Crawl()
    c.main()
