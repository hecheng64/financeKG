'''
Author: WenZeng Cai
Date: 2021-03-06 15:37:56
LastEditors: your name
LastEditTime: 2021-03-06 21:02:08
Description: 利用BaiDu LAC对非结构化数据进行处理
'''
from LAC import LAC


class UnstructuredData:
    def __init__(self,trainfile,testfile,modelpath,lac):
        self.trainfile = trainfile
        self.testfile = testfile
        self.modelpath = modelpath
        self.lac = lac
        
        
    def _incrementalTrain(self):
        '''
        description: 增量训练，待定
        param {*}
        return {*}
        '''
        self.lac.train(model_save_dir='./finance_lac_model/',
                       train_data=self.trainfile, test_data=self.testfile)
    
    def _dataPreProcessing(self):
        '''
        description: 用于处理增量训练数据，待定
        param {*}
        return {*}
        '''
        pass

   






   

        
        

if __name__ == "__main__":
    pass

