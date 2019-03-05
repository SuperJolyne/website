package elas;

import com.google.gson.Gson;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jijngbao
 * @date 19-1-23
 */
public class SearchTitan {
    private BoolQueryBuilder boolQueryBuilder;
    private Goods goods=null;
    private void init(String titan){
        Gson gson=new Gson();
        goods=gson.fromJson(titan,Goods.class);
    }
    public QueryBuilder createBuilder(String titan){
        init(titan);
        Class c = null;
        try {
            c = Class.forName(goods.getClass().getName());
            Field[] fields = c.getDeclaredFields();
            for (Field f : fields) {
                String propname = f.getName();
                String methodname = "get" +
                        propname.substring(0, 1).toUpperCase()
                        + propname.substring(1, propname.length());
                Method method = c.getMethod(methodname);
                Object temp= method.invoke(goods);
                if (temp!=null&&!temp.equals("")){
                    System.out.println(propname);
                    System.out.println(temp);
                    build(propname, (String) temp);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return boolQueryBuilder;

    }

    private void build(String fieldName,String fieldValue){
        System.out.println("fn:"+fieldName);
        System.out.println("fv:"+fieldValue);
        if (boolQueryBuilder==null){
            boolQueryBuilder=QueryBuilders.boolQuery().must(QueryBuilders
                    .matchPhraseQuery(fieldName,fieldValue));
        }else {
            boolQueryBuilder=boolQueryBuilder.must(QueryBuilders
                    .matchPhraseQuery(fieldName,fieldValue));
        }

    }

    public BoolQueryBuilder mustdBuild(String json){
        SearchCtn ctn=new Gson().fromJson(json,SearchCtn.class);
        String data=ctn.getCtn();
        String temp="";
        boolean flag=true;
        if((temp=isOne(data))!=null){
            if (boolQueryBuilder==null){
                boolQueryBuilder=QueryBuilders.boolQuery().must(QueryBuilders
                        .matchPhraseQuery("one",temp));
            }
            data=data.replace(temp,"");
        }

        if((temp=isTwo(data))!=null){
            if (boolQueryBuilder==null){

                boolQueryBuilder=QueryBuilders.boolQuery().must(QueryBuilders
                        .matchPhraseQuery("two",temp));
            }else {
                boolQueryBuilder=boolQueryBuilder.must(QueryBuilders
                        .matchPhraseQuery("two",temp));
            }
            data=data.replace(temp,"");
        }

        if((temp=isThree(data))!=null){
            if (boolQueryBuilder==null){

                boolQueryBuilder=QueryBuilders.boolQuery().must(QueryBuilders
                        .matchPhraseQuery("three",temp));
            }else {
                boolQueryBuilder=boolQueryBuilder.must(QueryBuilders
                        .matchPhraseQuery("three",temp));
            }
            data=data.replace(temp,"");
        }

        if((temp=isBrand(data))!=null){
            if (boolQueryBuilder==null){
                boolQueryBuilder=QueryBuilders.boolQuery().must(QueryBuilders
                        .matchPhraseQuery("brand",temp));
            }else {
                boolQueryBuilder=boolQueryBuilder.must(QueryBuilders
                        .matchPhraseQuery("brand",temp));
            }
            data=data.replace(temp,"");
        }

        if((temp=isPrince(data))!=null){
            if (Integer.parseInt(temp)>50){
                flag=false;
                if (boolQueryBuilder==null){
                    boolQueryBuilder=QueryBuilders.boolQuery().must(QueryBuilders
                            .matchPhraseQuery("price",temp));
                }else {
                    boolQueryBuilder=boolQueryBuilder.must(QueryBuilders
                            .matchPhraseQuery("price",temp));
                }
            }
            data=data.replace(temp,"");

        }

        if((temp=isSize(data))!=null&&flag){
            if (boolQueryBuilder==null){
                boolQueryBuilder=QueryBuilders.boolQuery().must(QueryBuilders
                        .matchPhraseQuery("llen",temp));
            }else {
                boolQueryBuilder=boolQueryBuilder.must(QueryBuilders
                        .matchPhraseQuery("llen",temp));
            }
            data=data.replace(temp,"");
        }

        if ((temp=isColor(data))!=null){
            if (boolQueryBuilder==null){

                boolQueryBuilder=QueryBuilders.boolQuery().must(QueryBuilders
                        .matchPhraseQuery("color",temp));
            }else {
                boolQueryBuilder=boolQueryBuilder.must(QueryBuilders
                        .matchPhraseQuery("color",temp));
            }
            data=data.replace(temp,"");
        }
        if (data!=null && !data.equals("")){
            if (boolQueryBuilder==null){
                boolQueryBuilder=QueryBuilders.boolQuery().must(QueryBuilders
                        .matchPhraseQuery("title",data));
            }else {
                boolQueryBuilder=boolQueryBuilder.must(QueryBuilders
                        .matchPhraseQuery("title",data));
            }
        }


        return boolQueryBuilder;
    }

    private String isOne(String data){
        String sex = "";
        Pattern pattern1 = Pattern.compile("(男)");
        Matcher matcher1=pattern1.matcher(data);
        if (matcher1.find()){
            sex = "男士";
            return sex;
        }
        Pattern pattern2 = Pattern.compile("(女)");
        Matcher matcher2=pattern2.matcher(data);
        if (matcher2.find()){
            sex = "女士";
            return sex;
        }
        Pattern pattern3 = Pattern.compile("(儿童)");
        Matcher matcher3=pattern3.matcher(data);
        if (matcher3.find()){
            sex = "儿童";
            return sex;
        }
        return null;
    }

    private String isTwo(String data){
        Pattern pattern = Pattern.compile("(服装)|(鞋子)|(包/箱)|(其他)");
        Matcher matcher=pattern.matcher(data);
        if (matcher.find()){
            return matcher.group();
        }
        return null;
    }

    private String isThree(String data){
        Pattern pattern = Pattern.compile("(系带鞋)|(裙子)|(沙漠靴)|(首饰)|(手拿包)|" +
                "(旅行包和行李箱)|(浅口细高跟鞋)|(运动鞋)|(化妆包)|(短裤和中裤)|(羽绒服)|(一脚蹬休闲鞋)|(连衣裙)|(牛仔裤)|(包袋配饰)|(腰带)|(香水)|(芭蕾平底鞋)|(T恤和上衣)|(邮差包)|(针织衫)|(1)|(腰包和双肩包)|(运动衫)|(靴子和踝靴)|(无边针织帽和有沿帽)|(盥洗包)|(大衣和夹克)|(手提包)|(泳装)|(草编鞋)|(卡包)|(休闲西装)|(太阳镜)|(皮草)|(长裤)|(单肩包)|(手套)|(钱包)|(乐福鞋和莫卡辛鞋)|(凉鞋)|(时尚数码配饰)|(紧身打底裤)|(拖鞋和夹脚拖鞋)|(衬衫)|(领带)|(公文包)|(围巾和披肩)|(钥匙扣)|(背心)|(西服)");
        Matcher matcher=pattern.matcher(data);
        if (matcher.find()){
            return matcher.group();
        }
        return null;
    }

    private String isBrand(String data){
        Pattern pattern = Pattern.compile("(JOHN RICHMOND)|(NOBILE 1942)|" +
                "(CREED)|(LANVIN PARIS)|(LOTTO)|(DIOR)|(RED VALENTINO)|" +
                "(CARNER BARCELONA)|(JOVOY PARIS)|(HALMANERA)|(ASFVLT)|(DEAR ROSE)|(CULT GAIA)|(MARCELO BURLON)|(FURLA)|(BOTTEGA VENETA)|(MOA MASTER OF ARTS)|(MARC JACOBS)|(HOGAN)|(ASPINALS LONDON)|(EVODY)|(GUCCI)|(M. MICALLEF)|(CODELLO)|(HUNTER)|(TOD'S)|(JIMMY CHOO)|(GOLDEN GOOSE)|(VICTORIA BECKHAM)|(DIADORA)|(BALENCIAGA)|(TS PASHMINA NEPAL)|(ROBERT PIGUET)|(IL PROFVMO)|(DSQUARED2)|(FARMACIA ANNUNZIATA)|(PERRISE CARLO)|(NISHANE ISTANBUL)|(LAURENT MAZZONE)|(MANZONI)|(GCDS)|(CHLOE)|(ARTE PROFUMI ROMA)|(ASH)|(HOGAN)|(ALEXANDER MCQUEEN)|(MIU MIU)|(ERMANNO SCERVINO)|(FRANCK BOCLET)|(SALVATORE FERRAGAMO)|(TRUSSARDI)|(KARL LAGERFELD)|(ALEXANDER WANG)|(RALPH LAUREN)|(MCM)|(CESARE PACIOTTI)|(SAUF)|(SPEKTRA PARIS)|(JEROBOAM)|(CHIARA FERRAGNI)|(PROENZA SCHOULER)|(DOLCE & GABBANA)|(NEBBIA)|(RAF SIMONS)|(SERGIO LEVANTESI)|(ARMANI)|(PREMIATA)|(ANTONIO ALESSANDRIA)|(ALCHIMISTA)|(ARMANI)|(TORY BURCH)|(STELLA MCCARTNEY)|(BARK)|(CHURCH'S)|(PRADA)|(BURBERRY)|(STUART WEITZMAN)|(MONCLER)|(BLUGIRL)|(Y-3)|(KEIKO MECHERI)|(ODIN NEW YORK)|(HOLUBAR)|(D'ESTE)|(FENDI)|(LONGCHAMP)|(MOSCHINO)|(DIADORA)|(VALSPORT 1920)|(PHILIPPE MODEL)|(VALENTINO)|(LE GALION)|(UNUM)|(PATRIZIA PEPE)|(OFFICINA DELLE ESSENZE)|(GIVENCHY)|(ISABEL MARANT)|(CLARKS)|(MOLINARD)|(FRANCESCA DELL' ORO)|(RICK OWENS)|(SULTANE DE SABA)|(CAR SHOE)|(ETAT LIBRE D'ORANGE)|(MOU)|(VERSACE)|(FRED PERRY)|(BALLY)|(PHILIPP PLEIN)");
        Matcher matcher=pattern.matcher(data);
        if (matcher.find()){
            return matcher.group();
        }
        return null;
    }

    private String isSize(String data){
        Pattern pattern = Pattern.compile("(S)|(M)|(L)|(XL)|(XXL)|(XXXL)|" +
                "(XXXXL)|(15)|(16)|(17)|(18)|(19)|(20)|(21)|(22)|(23)|(24)|(25)|(26)|(27)|(28)|(29)|(30)|(31)|(32)|(33)|(34)|(35)|(36)|(37)|(38)|(39)|(40)|(41)|(42)|(43)|(44)|(45)|(46)|(47)|(48)|(49)|(50)|(20寸)|(22寸)|(24寸)|(26寸)|(29寸)");
        Matcher matcher=pattern.matcher(data);
        if (matcher.find()){
            return matcher.group();
        }
        return null;
    }

    private String isColor(String data){
        Pattern pattern = Pattern.compile("(灰色)|(红色)|(白色)|(蓝色)|(黑色)|(紫罗兰色)|" +
                "(米色)|(棕色)|(粉色)|(金色)|(橙色)|(绿色)|(淡蓝色)|(银色)|(黄色)");
        Matcher matcher=pattern.matcher(data);
        if (matcher.find()){
            return matcher.group();
        }
        return null;
    }

    private void analyzer(String json){
        SearchCtn ctn=new Gson().fromJson(json,SearchCtn.class);
    }
    private String isPrince(String data){
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher=pattern.matcher(data);
        if (matcher.find()){
            return matcher.group();
        }
        return null;
    }

    public static void main(String[] args) {
        String s="系带鞋\\| 裙子\\| 沙漠靴\\| 首饰\\| 手拿包\\| 旅行包和行李箱\\| 浅口细高跟鞋\\| " +
                "运动鞋\\| 化妆包\\| 短裤和中裤\\| 羽绒服\\| 一脚蹬休闲鞋\\| 连衣裙\\| 牛仔裤\\| 包袋配饰\\| 腰带\\| 香水\\| 芭蕾平底鞋\\| T恤和上衣\\| 邮差包\\| 针织衫\\| 1\\| 腰包和双肩包\\| 运动衫\\| 靴子和踝靴\\| 无边针织帽和有沿帽\\| 盥洗包\\| 大衣和夹克\\| 手提包\\| 泳装\\| 草编鞋\\| 卡包\\| 休闲西装\\| 太阳镜\\| 皮草\\| 长裤\\| 单肩包\\| 手套\\| 钱包\\| 乐福鞋和莫卡辛鞋\\| 凉鞋\\| 时尚数码配饰\\| 紧身打底裤\\| 拖鞋和夹脚拖鞋\\| 衬衫\\| 领带\\| 公文包\\| 围巾和披肩\\| 钥匙扣\\| 背心\\| 西服";s=s.replace("|","+-");
        s=s.replace(" ","");
        s=s.replace("+","|");
        System.out.println(s.replace("-","(").replace("\\",")").trim());
        Pattern pattern = Pattern.compile
                ("(S)|(XXL)|(20寸)");
        Matcher matcher=pattern.matcher("20寸哈哈哈SLXXL");
        System.out.println(matcher.matches());
        if (matcher.find()){
            System.out.println(matcher.group(1));
        }

        Pattern pattesrn = Pattern.compile("(系带鞋)|( 裙子)|( 沙漠靴)|( 首饰)|( 手拿包)|(" +
                " 旅行包和行李箱)|( 浅口细高跟鞋)|( 运动鞋)|( 化妆包)|( 短裤和中裤)|( 羽绒服)|( 一脚蹬休闲鞋)|" +
                "( 连衣裙)|( 牛仔裤)|( 包袋配饰)|( 腰带)|( 香水)|( 芭蕾平底鞋)|( T恤和上衣)|( 邮差包)|( 针织衫)|( 1)|( 腰包和双肩包)|( 运动衫)|( 靴子和踝靴)|( 无边针织帽和有沿帽)|( 盥洗包)|( 大衣和夹克)|( 手提包)|( 泳装)|( 草编鞋)|( 卡包)|( 休闲西装)|( 太阳镜)|( 皮草)|( 长裤)|( 单肩包)|( 手套)|( 钱包)|( 乐福鞋和莫卡辛鞋)|( 凉鞋)|( 时尚数码配饰)|( 紧身打底裤)|( 拖鞋和夹脚拖鞋)|( 衬衫)|( 领带)|( 公文包)|( 围巾和披肩)|( 钥匙扣)|( 背心)|( 西服)\n");
        Matcher sa=pattesrn.matcher("忈DIOR");
        if (sa.find()){
            System.out.println(sa.group());
        }


        Pattern xx = Pattern.compile("\\d+");
        Matcher xxx=xx.matcher("jianca66876");
        if (xxx.find()){
            System.out.println(xxx.group());
        }
        String temp="灰色\n" +
                "红色\n" +
                "白色\n" +
                "蓝色\n" +
                "黑色\n" +
                "紫罗兰色\n" +
                "米色\n" +
                "棕色\n" +
                "粉色\n" +
                "金色\n" +
                "橙色\n" +
                "绿色\n" +
                "淡蓝色\n" +
                "银色\n" +
                "黄色";
        System.out.println(temp.replace("\n","+-").replace("+",")")
                .replace("-","|-").replace("-","("));




    }


}
