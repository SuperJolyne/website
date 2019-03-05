package elas;
public class Goods extends MyJson{
    public Goods(){}
    private String gid=null;

    public String getGid(){
        return this.gid;
    }

    public void setGid(String value){
        this.gid= value;
    }

    private String one=null;

    public String getOne(){
        return this.one;
    }

    public void setOne(String value){
        this.one= value;
    }

    private String two=null;

    public String getTwo(){
        return this.two;
    }

    public void setTwo(String value){
        this.two= value;
    }

    private String three=null;

    public String getThree(){
        return this.three;
    }

    public void setThree(String value){
        this.three= value;
    }

    private String brand=null;

    public String getBrand(){
        return this.brand;
    }

    public void setBrand(String value){
        this.brand= value;
    }

    private String title=null;

    public String getTitle(){
        return this.title;
    }

    public void setTitle(String value){
        this.title= value;
    }

    private String description=null;

    public String getDescription(){
        return this.description;
    }

    public void setDescription(String value){
        this.description= value;
    }

    private String discount=null;

    public String getDiscount(){
        return this.discount;
    }

    public void setDiscount(String value){
        this.discount= value;
    }

    private String price=null;

    public String getPrice(){
        return this.price;
    }

    public void setPrice(String value){
        this.price= value;
    }

    private String pro_code=null;

    public String getPro_code(){
        return this.pro_code;
    }

    public void setPro_code(String value){
        this.pro_code= value;
    }

    private String color=null;

    public String getColor(){
        return this.color;
    }

    public void setColor(String value){
        this.color= value;
    }

    private String size=null;

    public String getSize(){
        return this.size;
    }

    public void setSize(String value){
        this.size= value;
    }

    private String im=null;

    public String getIm(){
        return this.im;
    }

    public void setIm(String value){
        this.im= value;
    }

    public static Goods getGoods(String [] data){
        Goods obj=new Goods();
        obj.setGid(data[0]);
        obj.setOne(data[1]);
        obj.setTwo(data[2]);
        obj.setThree(data[3]);
        obj.setBrand(data[4]);
        obj.setTitle(data[5]);
        obj.setDescription(data[6]);
        obj.setDiscount(data[7]);
        obj.setPrice(data[8]);
        obj.setPro_code(data[9]);
        obj.setColor(data[10]);
        obj.setSize(data[11]);
        obj.setIm(data[12]);
        return obj;
    }
}