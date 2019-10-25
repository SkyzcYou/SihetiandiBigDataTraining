#!/home/lvgang/.pyenv/shims/python
# -*- coding: utf-8 -*-
# @Date    : 2018-10-22 09:41:29
# @Author  : LvGang/Garfield
# @Email   : Garfield_lv@163.com


import os
import time
import socket
import datetime

class Flume_test(object):
    def  __init__(self):
        self.flume_host ='192.168.3.101'
        self.flume_port = 55555

    def gen_conn(self):
        tcp_cli = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
        return tcp_cli

    def gen_data(self):
        return 'Flume test ,datetime:[%s]\n'%datetime.datetime.now()

    def main(self):
        cli = self.gen_conn()
        cli.connect((self.flume_host,self.flume_port))
        while 1:
                data = self.gen_data()
                print(data)
                cli.sendall(bytes(data, encoding="utf8"))
                recv = cli.recv(1024)
                print(recv)
                time.sleep(1)
        s.close()

if __name__ == '__main__':
    ft = Flume_test()
    ft.main()
