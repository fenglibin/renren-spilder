package it.renren.spilder.main;

public class Environment {

    // �Ƿ�ֻ��ȡ��ǰ�����ļ��ĵ�һҳ
    public static boolean dealOnePage                            = false;
    // ���ڱ�ʾ���Ը÷�������������ļ��Ƿ��ܹ�������������λ�������ݿ�Ĳ���������һ�������ļ�ֻ���һ����¼
    public static boolean checkConfigFile                        = false;
    // ��ʹ�ô���
    public static boolean isUseProxy                             = false;
    // �����������ַ
    public static String  proxy                                  = null;
    // ��ǰ׺
    public static String  tablePrefix                            = null;
    // ���������£�ʧ�ܵ���һ��������ʱ���˳����Ʊ�ʶ
    public static boolean exitByFailedDealPages                  = Boolean.TRUE;
    // �Ǵ�������ʱ���Ƿ��ӡͬ��ȡ��������
    public static boolean isOutputHtmlContentWhenErrorHappend    = Boolean.FALSE;
    // �ڱ���ͼƬʱ���Ƚ����뵱ǰHTML��TITLE��ͬ��Ŀ¼���ٱ��浱ǰͼƬ����Ŀ¼�У�Ĭ��ֵΪfalse
    public static boolean isSaveImage2CurrentHtmlFileTileNameDir = Boolean.FALSE;
    // �Ƿ�ͼƬվ��������ڿ����ڻ�ȡ��ÿƬ���¶�����ͷ�����ؼ�
    public static boolean isImageSite                            = Boolean.FALSE;
    // Ϊ�ɼ���ͼƬ����ˮӡ��ͼƬ��ַ
    public static String  waterImageLocation                     = null;
    // ���cook���ݵ��ļ�
    public static String  cookFile                               = null;
}
