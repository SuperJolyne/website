package util;

import Osscli.App;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import pojo.D;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ManageCid {
    public static void doAdd(D dd, String id, String type, String cid, String nm, String oid) throws IOException {
        String gid = dd.getGid();
        String com = dd.getCom();
        String siz = dd.getSiz();
        String qua = dd.getQua();
        String nam = dd.getNam();

        OSSClient ossClient = App.getOSS();
        String bucketName = "superljc";
        String touName = type + id + ".json";
        boolean found = ossClient.doesObjectExist(bucketName, touName);
        System.out.println("found:" + found);

        //上传到用户部分
        if (found) {

            String touString = "";
            OSSObject touObject = ossClient.getObject(bucketName, touName);
            BufferedReader touReader = new BufferedReader(new InputStreamReader(touObject.getObjectContent()));
            while (true) {
                //读取json评价头文件
                String line = touReader.readLine();
                if (line == null) {
                    break;
                } else {
                    touString += line;
                }
                touString.trim();
                System.out.println(touString);
            }
            JSONObject touJson = JSONObject.parseObject(touString);
            String tl = touJson.getString("tl");//最后一个json评价地址
            String tll[] = tl.split("[/]");
            String tl_cid = tll[tll.length - 1];//获取地址中的json格式
            System.out.println("tl_cid:" + tl_cid);
            File tou_f = new File("/home/super/Business/" + id + ".json");
            RandomAccessFile tou_raf = new RandomAccessFile(tou_f, "rw");
            tou_raf.seek(0);

            //读取最后一个json评价
            OSSObject tl_json = ossClient.getObject(bucketName, tl);
            BufferedReader tl_Reader = new BufferedReader(new InputStreamReader(tl_json.getObjectContent()));
            String tlString = "";
            while (true) {
                //读取json评价头文件
                String line = tl_Reader.readLine();
                if (line == null) {
                    break;
                } else {
                    tlString += line;
                }
                tlString.trim();
            }
            System.out.println(tlString);
            JSONObject jsonObject1 = JSONObject.parseObject(tlString);

            File tl_f = new File("/home/super/Business/" + tl_cid);
            RandomAccessFile tl_raf = new RandomAccessFile(tl_f, "rw");
            tl_raf.seek(0);

            //新评论文件
//
            File new_f = new File("/home/super/Business/" + cid + ".json");
            RandomAccessFile new_raf = new RandomAccessFile(new_f, "rw");
            new_raf.seek(0);
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("fr", tl);
            JSONObject ctn = new JSONObject();
            ctn.put("cid", cid);
            ctn.put("gid", gid);
            ctn.put("com", com);
            ctn.put("siz", siz);
            ctn.put("qua", qua);
            ctn.put("nam", nam);
            ctn.put("nm", nm);
            jsonObject2.put("ctn", ctn);
            jsonObject2.put("nx", "0");

            System.out.println("json:" + jsonObject2.toJSONString());
            new_raf.write(jsonObject2.toJSONString().getBytes());
            new_raf.close();
            //将新评论上传到文件中
            String newjson = type + cid + ".json";
            ossClient.putObject(bucketName, newjson, new_f);
            new_f.delete();

            //写入日志系统
            Pattern p = Pattern.compile("(user:)");
            Matcher m = p.matcher(id);
            if (m.find()){
                File lof_f = new File("/home/super/Business/log/pl/pl.log");
                RandomAccessFile log_raf = new RandomAccessFile(lof_f, "rw");
                log_raf.seek(lof_f.length());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                String now = sdf.format(new Date());
                long time = System.currentTimeMillis();
                JSONObject log_json = new JSONObject();
                log_json.put("cid", cid);
                log_json.put("uid", id);
                log_json.put("nm", nm);
                log_json.put("oid", oid);
                log_json.put("com", com);
                log_json.put("now", now);
                log_json.put("time", time);
                log_raf.write((log_json.toJSONString()+"\n").getBytes());
                log_raf.close();
            }

            jsonObject1.put("nx", newjson);
            tl_raf.write(jsonObject1.toJSONString().getBytes());
            tl_raf.close();
            ossClient.putObject(bucketName, tl, tl_f);
            tl_f.delete();

            touJson.put("tl", newjson);
            tou_raf.write(touJson.toJSONString().getBytes());
            tou_raf.close();
            ossClient.putObject(bucketName, touName, tou_f);
            tou_f.delete();


            // 数据读取完成后，获取的流必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
            touReader.close();
            tl_Reader.close();
            ossClient.shutdown();


        } else {
            File tou_f = new File("/home/super/Business/" + id + ".json");
            RandomAccessFile tou_raf = new RandomAccessFile(tou_f, "rw");
            tou_raf.seek(0);
            JSONObject tou_json = new JSONObject();

            //评论文件
            String new_add = type + cid + ".json";
            File new_f = new File("/home/super/Business/" + cid + ".json");
            RandomAccessFile new_raf = new RandomAccessFile(new_f, "rw");
            new_raf.seek(0);
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("fr", touName);
            JSONObject ctn = new JSONObject();
            ctn.put("cid", cid);
            ctn.put("gid", gid);
            ctn.put("com", com);
            ctn.put("siz", siz);
            ctn.put("qua", qua);
            ctn.put("nam", nam);
            ctn.put("nm", nm);
            jsonObject2.put("ctn", ctn);
            jsonObject2.put("nx", "0");
            new_raf.write(jsonObject2.toJSONString().getBytes());
            new_raf.close();

            //写入日志系统
            Pattern p = Pattern.compile("(user:)");
            Matcher m = p.matcher(id);
            if (m.find()){
                File lof_f = new File("/home/super/Business/log/pl/pl.log");
                RandomAccessFile log_raf = new RandomAccessFile(lof_f, "rw");
                log_raf.seek(lof_f.length());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                String now = sdf.format(new Date());
                long time = System.currentTimeMillis();
                JSONObject log_json = new JSONObject();
                log_json.put("cid", cid);
                log_json.put("uid", id);
                log_json.put("nm", nm);
                log_json.put("oid", oid);
                log_json.put("com", com);
                log_json.put("now", now);
                log_json.put("time", time);
                log_raf.write((log_json.toJSONString()+"\n").getBytes());
                log_raf.close();
            }

            //上传新评价json
            ossClient.putObject(bucketName, new_add, new_f);
            new_f.delete();

            //上传头文件
            tou_json.put("hd", new_add);
            tou_json.put("tl", new_add);
            tou_raf.write(tou_json.toJSONString().getBytes());
            tou_raf.close();
            ossClient.putObject(bucketName, touName, tou_f);
            tou_f.delete();
            ossClient.shutdown();
        }
    }



    public static void doDelete(String cid_add, String fr, String nx, String id, String type, String cid) throws IOException {
        OSSClient ossClient = App.getOSS();
        String bucketName = "superljc";
        //该评论在链表中的最后一个
        if (nx.equals("0")) {

            //如果上一个文件为头文件，即该用户只有一个评论，则连同头文件一起删除
            Pattern p = Pattern.compile("(use:|goods:)");
            Matcher m = p.matcher(fr);
            if (m.find()) {
                ossClient.deleteObject(bucketName, fr);
                ossClient.deleteObject(bucketName, cid_add);
            } else {
                OSSObject fr_Object = ossClient.getObject(bucketName, fr);

                // 读取文件内容。
                String fr_String = "";
                BufferedReader fr_reader = new BufferedReader(new InputStreamReader(fr_Object.getObjectContent()));
                while (true) {
                    String line = fr_reader.readLine();
                    if (line == null) {
                        break;
                    } else {
                        fr_String += line;
                    }
                }
                // 数据读取完成后，获取的流必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
                fr_reader.close();
                //将fr写文件重新上传
                JSONObject fr_json = JSONObject.parseObject(fr_String);
                System.out.println(fr_json.getString("nx"));
                fr_json.put("nx", "0");
                String fr_l[] = fr.split("[/]");
                String fr_cid = fr_l[fr_l.length - 1];
                File fr_f = new File("/home/super/Business/" + fr_cid);
                RandomAccessFile fr_raf = new RandomAccessFile(fr_f, "rw");
                fr_raf.seek(0);
                fr_raf.write(fr_json.toJSONString().getBytes());
                fr_raf.close();
                ossClient.putObject(bucketName, fr, fr_f);
                fr_f.delete();


                //上传头文件
                String tou_add = type + id + ".json";
                OSSObject tou_Object = ossClient.getObject(bucketName, tou_add);

                // 读取文件内容
                String tou_String = "";
                BufferedReader tou_reader = new BufferedReader(new InputStreamReader(tou_Object.getObjectContent()));
                while (true) {
                    String line = tou_reader.readLine();
                    if (line == null) {
                        break;
                    } else {
                        tou_String += line;
                    }
                }
                // 数据读取完成后，获取的流必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
                tou_reader.close();
                //将头文件写文件重新上传
                JSONObject tou_json = JSONObject.parseObject(tou_String);
                tou_json.put("tl", fr);
                File tou_f = new File("/home/super/Business/" + id + ".json");
                RandomAccessFile tou_raf = new RandomAccessFile(tou_f, "rw");
                tou_raf.seek(0);
                tou_raf.write(tou_json.toJSONString().getBytes());
                tou_raf.close();
                ossClient.putObject(bucketName, tou_add, tou_f);
                tou_f.delete();
                ossClient.shutdown();

                //写入日志系统
                Pattern pattern = Pattern.compile("(user:)");
                Matcher matcher = pattern.matcher(id);
                if (matcher.find()){
                    File lof_f = new File("/home/super/Business/log/scpl/scpl.log");
                    RandomAccessFile log_raf = new RandomAccessFile(lof_f, "rw");
                    log_raf.seek(lof_f.length());
                    long time = System.currentTimeMillis();
                    JSONObject log_json = new JSONObject();
                    log_json.put("cid", cid);
                    log_json.put("time", time);
                    log_raf.write((log_json.toJSONString()+"\n").getBytes());
                    log_raf.close();
                }

            }
        }else {//该评论不为最后一个，将前后评论连接
            //将cid上一个评论的nx地址改为nx
            OSSObject fr_Object = ossClient.getObject(bucketName, fr);

            // 读取文件内容。
            String fr_String = "";
            BufferedReader fr_reader = new BufferedReader(new InputStreamReader(fr_Object.getObjectContent()));
            while (true) {
                String line = fr_reader.readLine();
                if (line == null) {
                    break;
                } else {
                    fr_String += line;
                }
            }
            // 数据读取完成后，获取的流必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
            fr_reader.close();
            //将fr写文件重新上传
            JSONObject fr_json = JSONObject.parseObject(fr_String);
            System.out.println(fr_json.getString("nx"));
            fr_json.put("nx", nx);
            String fr_l[] = fr.split("[/]");
            String fr_cid = fr_l[fr_l.length - 1];
            File fr_f = new File("/home/super/Business/" + fr_cid);
            RandomAccessFile fr_raf = new RandomAccessFile(fr_f, "rw");
            fr_raf.seek(0);
            fr_raf.write(fr_json.toJSONString().getBytes());
            fr_raf.close();
            ossClient.putObject(bucketName, fr, fr_f);
            fr_f.delete();

            //cid下一个评论的fr地址改为fr
            OSSObject nx_Object = ossClient.getObject(bucketName, nx);
            // 读取文件内容。
            String nx_String = "";
            BufferedReader nx_reader = new BufferedReader(new InputStreamReader(nx_Object.getObjectContent()));
            while (true) {
                String line = nx_reader.readLine();
                if (line == null) {
                    break;
                } else {
                    nx_String += line;
                }
            }
            // 数据读取完成后，获取的流必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
            nx_reader.close();
            //将fr写文件重新上传
            JSONObject nx_json = JSONObject.parseObject(nx_String);
            nx_json.put("fr", fr);

            String nx_l[] = nx.split("[/]");
            String nx_cid = nx_l[nx_l.length - 1];
            File nx_f = new File("/home/super/Business/" + nx_cid);
            RandomAccessFile nx_raf = new RandomAccessFile(nx_f, "rw");
            nx_raf.seek(0);
            nx_raf.write(fr_json.toJSONString().getBytes());
            nx_raf.close();
            ossClient.putObject(bucketName, nx, nx_f);
            nx_f.delete();
            ossClient.shutdown();

            //写入日志系统
            Pattern pattern = Pattern.compile("(user:)");
            Matcher matcher = pattern.matcher(id);
            if (matcher.find()){
                File lof_f = new File("/home/super/Business/log/scpl/scpl.log");
                RandomAccessFile log_raf = new RandomAccessFile(lof_f, "rw");
                log_raf.seek(lof_f.length());
                long time = System.currentTimeMillis();
                JSONObject log_json = new JSONObject();
                log_json.put("cid", cid);
                log_json.put("time", time);
                log_raf.write((log_json.toJSONString()+"\n").getBytes());
                log_raf.close();
            }
        }
    }
}
