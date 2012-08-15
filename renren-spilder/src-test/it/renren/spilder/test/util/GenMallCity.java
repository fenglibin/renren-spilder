package it.renren.spilder.test.util;

/**
 * 生成糯米网城市数据 类
 * 
 * @author Administrator 2012-8-15 下午10:31:02
 */
public class GenMallCity {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String citys = "chaoyang, wenzhou, yingkou, yantai, xiangyang, yiyang, suqian, hulunbeier, nyingchi, haidong, qujing, wuxi, dongying, chenzhou, jian, huaihua, yangzhou, qiandongnanmiaodongautonomous, kaifeng, shanghai, xinxiang, hengyang, jinzhou, sanya, yinchuan, cixi, luzhou, heze, taiyuan, luoyang, rikaze, yingtan, beijing, jinchang, mudanjiang, kunming, daqing, shuangyashan, jiaxing, taian, tacheng, chengdu, changshu, nujianglisuzu, shaoyang, xianning, hefei, bazhong, beihai, suihua, liaocheng, liaoyang, weihai, liaoyuan, qitaihe, jiamusi, yanan, qingdao, dongguan, tongchuan, changdu, yichun1, hebi, benxi, huangshan, chaozhou, shuozhou, shanwei, hengshui, liupanshui, baotou, dali, yibin, binzhou, deyang, qinhuangdao, haibei, aomen, tianshui, tongling, guangzhou, yichun, zhangye, langfang, baoji, baiyin, bayannaoer, jilin, jinhua, qinzhou, quzhou, naqu, puyang, chaohu, hohhot, artux, qingyuan, guiyang, anshun, altay, daxinganling, honghe, tangshan, nanping, quanzhou, hetian, haikou, xuzhou, eerduosi, zhumadian, linyi, loudi, laibin, chifeng, shijiazhuang, wuzhong, ningbo, fuyang, fangchenggang, hegang, nanjing, boertalamongol, chuxiong, yancheng, yushu, zhangjiakou, sanming, shangluo, yulin1, chuzhou, shizuishan, zhangzhou, bengbu, haerbin, zhaoqing, emeishan, jiangmen, xinyang, qingyang, lincang, jingmen, xingtai, anyang, suzhou, guangyuan, pingdingshan, maoming, bijie, yueyang, weinan, ziyang, jixi, changde, dandong, nanyang, jining, xuchang, chongqing, tianjin, pingliang, xiaogan, baicheng, wulanchabu, shenyang, yangjiang, qianxinan, ganzhou, xishuangbanna, ankang, diqing, xiangxi, bayangolmongol, shannan, xining, tieling, fuzhou1, chizhou, yili, alashan, tonghua, huangshi, wulumuqi, putian, puer, kashgar, dazhou, laiwu, changzhi, handan, siping, anqing, qiqihar, tongren, xilinguole, shaoxing, kelamayi, fuxin, shantou, suzhou1, nantong, liangshan, jiaozuo, fuzhou, meishan, hezhou, yunfu, jincheng, ali, hanzhong, panzhihua, yangquan, yuncheng, akesu, huizhou, xianggang, haixi, zigong, xianyang, fushun, xiamen, hechi, jiuquan, aba, daidehongjingpo, jinzhong, tanggu, zhaotong, huaibei, baishan, changsha, jieyang, mianyang, tongliao, anshan, chongzuo, guoluo, baise, zhoushan, lishui, wujiang, dezhou, linfen, anqiu, xingan, huainan, huludao, qiannan, linxia, lasa, jinan, shaoguan, dalian, cangzhou, huangnan, lvliang, zhuhai, hangzhou, yulin, wenshan, zhongwei, huzhou, jindezhen, dingxi, jiayuguan, zhoukou, suizhou, shenzhen, xuancheng, shangrao, heihe, zunyi, tulufan, xinyu, wuhan, suining, yichang, baoshan, huaian, baoding, gannan, longyan, maanshan, nanchang, datong, zhangjiajie, taizhoux, wuhai, songyuan, longkou, liuan, guilin, qizhou, zhengzhou, zhuzhou, panjin, yaan, shiyan, wuhu, chengde, zhanjiang, yiwu, jiujiang, zibo, wuzhou, luohe, guigang, ganzizhou, changji, changchun, changzhou, wuwei, guyuan, weifang, yanbian, hami, zhenjiang, xiangtan, lanzhou, yongzhou, zaozhuang, pingxiang, longnan, yuxi, nanning, guangan, rizhao, zhongshan, neijiang, meizhou, shangqiu, taizhou, lijiang, xian, bozhou, kunshan, hainantibetan, lianyungang, nanchong, heyuan, liuzhou, foshan, sanmenxia, leshan, ningde";
        String[] cityArr = citys.split(",");
        int index = 0;
        StringBuilder sb = new StringBuilder("");
        sb.append("<?php\n");
        sb.append(" return array (\n");
        for (String city : cityArr) {
            sb.append("  ").append(index).append(" => ").append("'").append(city.trim()).append("'");
            index++;
            if (index < cityArr.length) {
                sb.append(",");
            }
            sb.append("\n");
        }
        sb.append(");\n");
        sb.append("?>");
        System.out.println(sb.toString());
    }
}
