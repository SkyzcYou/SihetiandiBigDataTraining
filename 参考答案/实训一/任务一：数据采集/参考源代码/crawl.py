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
        file_dir = '/home/developer/Desktop/Pr_Task1_2'
        file_name = 'Pr_Task1_2-%s-crawl_data.json'%str(datetime.datetime.now().date())
        file_path = os.path.join(file_dir,file_name)
        self.fp = open(file_path,'w',encoding='utf-8')
        self.fp.write('{\n')
        print('初始化成功')


    def response_handler(self,url,data):
        '''
        说明：使用目标网页Url或接口构造Response对象
        参数：url,目标网页的Url,数据类型String
        返回值：response,Response对象,数据类型Response Object
        '''
        print('开始构造响应')
        response = requests.post(url=url,data=data,headers=self.headers)
        return response
        print('构造响应成功')
        

    def parse(self,response):
        '''
        说明：对Response对象进行解析，形成结构化数据
        参数：response,Response对象,数据类型Response Object
        返回值：items,解析得到的数据，数据类型Object
        '''
        print('正在解析数据')
        print(response.text)
        items = []
        datas = json.loads(response.text).get('data',[])
        for data in datas:
            item = {}
            item['name'] = data['commodity_name']
            item['price'] = data['commodity_price']
            items.append(item)
        return items
        print('解析数据完成')


    def save_data(self,item):
        '''`
        说明：将解析到的数据存储到指定目录下的Json文件中去
        参数：item,一条数据,数据类型Dict
        返回值：无
        '''
        data = json.dumps(item,ensure_ascii=False)
        print('正在保存数据%s'%data)
        self.fp.write(data+',\n')


    def main(self):
        '''
        说明：主函数，处理逻辑编写的地方
        参数：无
        返回值：无
        '''
        url = 'http://192.168.3.124:28080/LexianMall/special/findSpecialCommodityBySpecialId.do'
        data = {
            'specialId': 5,
            'count': 7,
            'os': 'pc',
            'platformCode': 2,
            'unixtime': int(time.time())
        }
        print('开始进行采集，目标网址URL或接口为：%s'%url)
        response = self.response_handler(url,data)
        items = self.parse(response)
        for item in items:
            self.save_data(item)
        self.fp.write('}')
        self.fp.close()
        print('采集完成')


if __name__ == '__main__':
    c=Crawl()
    c.main()
