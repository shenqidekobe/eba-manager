import cz.vutbr.web.css.MediaSpec;
import com.microBusiness.manage.entity.Admin;
import com.microBusiness.manage.entity.Goods;
import com.microBusiness.manage.entity.Sn;
import com.microBusiness.manage.entity.ass.AssGoods;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.util.CollectionUtil;
import org.fit.cssbox.css.CSSNorm;
import org.fit.cssbox.css.DOMAnalyzer;
import org.fit.cssbox.demo.ImageRenderer;
import org.fit.cssbox.io.DOMSource;
import org.fit.cssbox.io.DefaultDOMSource;
import org.fit.cssbox.io.DefaultDocumentSource;
import org.fit.cssbox.io.DocumentSource;
import org.fit.cssbox.layout.BrowserCanvas;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.io.*;
import java.net.URL;
import java.text.AttributedCharacterIterator;
import java.util.*;
import java.util.List;

public class OtherTest {


    public static void main(String[] args) throws IOException, SAXException {

        int aaaa = 99 ;
        if(aaaa > 100){
            System.out.println(1);
        }else if(aaaa>1){
            System.out.println(2);
        }else if(aaaa>2){
            System.out.println(3);
        }else{
            System.out.println(4);
        }


        //后台的商品
        List<Goods> admins = new ArrayList<Goods>(){{
            this.add(new Goods(){{
                this.setId(1L);
                //this.setId(2L);
            }});

            this.add(new Goods(){{
                this.setId(4L);
                //this.setId(2L);
            }});

            this.add(new Goods(){{
                this.setId(3L);
                //this.setId(2L);
            }});
        }};

        //前端的商品
        List<Goods> goods2 = new ArrayList<Goods>(){{
            this.add(new Goods(){{
                this.setId(1L);
                //this.setId(3L);
            }});

            this.add(new Goods(){{
                this.setId(2L);
                //this.setId(3L);
            }});

            this.add(new Goods(){{
                this.setId(3L);
                //this.setId(3L);
            }});
        }};

        //新增的
        //admins.removeAll(goods2);

        List<Goods> addGoods = (List<Goods>)CollectionUtils.subtract(admins , goods2);

        List<Goods> delGoods = (List<Goods>)CollectionUtils.subtract(goods2 , admins);

        List<Goods> updateGoods = (List<Goods>)CollectionUtils.retainAll(goods2 , admins);



        //删除的
        //goods2.removeAll(admins) ;

        //修改的
       // admins.retainAll(goods2) ;

        //CollectionUtils.subtract()


        //admins.retainAll(admins2) ;



        /*String[] arrayA = new String[] { "1", "2", "3", "3", "4", "5" };
        String[] arrayB = new String[] { "3", "4", "4", "5", "6", "7" };

        List a = Arrays.asList( arrayA );
        List b = Arrays.asList( arrayB );

        Collection union = CollectionUtils.union( a, b );  //并集
        Collection intersection = CollectionUtils.intersection( admins, admins2); //交集
        Collection disjunction = CollectionUtils.disjunction( a, b ); //析取
        Collection subtract = CollectionUtils.subtract( a, b ); //差集



        System.out.println( "A: " + ArrayUtils.toString( a.toArray( ) ) );
        System.out.println( "B: " + ArrayUtils.toString( b.toArray( ) ) );
        System.out.println( "Union: " + ArrayUtils.toString( union.toArray( ) ) );
        System.out.println( "Intersection: " +
                ArrayUtils.toString( intersection.toArray( ) ) );
        System.out.println( "Disjunction: " +
                ArrayUtils.toString( disjunction.toArray( ) ) );
        System.out.println( "Subtract: " + ArrayUtils.toString( subtract.toArray( ) ) );*/



        System.out.println(1);




        String c = "中" ;
        byte[] cStr = c.getBytes("utf-8") ;

        String string = "abc\u5639\u563b";
        byte[] utf8 = string.getBytes("UTF-8");
// Convert from UTF-8 to Unicode
        string = new String(utf8, "UTF-8");
        System.out.println(string);

        System.out.println(OtherTest.class.getResource(""));
        System.out.println(OtherTest.class.getResource("/"));


        System.out.println(Thread.currentThread().getContextClassLoader().getResource(""));

        System.out.println(Thread.currentThread().getContextClassLoader().getResource("/"));
        System.exit(1);

        /*String urlstring = "http://test.dinghuo.me/ass/customerRelation/getPoster.jhtml?unionId=o1mHm0tirjka1xAEdgWCbtrae8X8&id=734";
        Dimension windowSize = new Dimension(700, 600);
        if(!urlstring.startsWith("http:") && !urlstring.startsWith("https:") && !urlstring.startsWith("ftp:") && !urlstring.startsWith("file:")) {
            urlstring = "http://" + urlstring;
        }

        String mediaType = "screen";
        FileOutputStream out = null;
        out = new FileOutputStream(new File("/Users/afei/aaa.png"));

        DocumentSource docSource = new DefaultDocumentSource(urlstring);
        DOMSource parser = new DefaultDOMSource(docSource);
        Document doc = parser.parse();
        MediaSpec media = new MediaSpec(mediaType);
        media.setDimensions((float)windowSize.width, (float)windowSize.height);
        media.setDeviceDimensions((float)700, (float)800);
        DOMAnalyzer da = new DOMAnalyzer(doc, docSource.getURL());
        da.setMediaSpec(media);
        da.attributesToStyles();
        da.addStyleSheet((URL)null, CSSNorm.stdStyleSheet(), DOMAnalyzer.Origin.AGENT);
        da.addStyleSheet((URL)null, CSSNorm.userStyleSheet(), DOMAnalyzer.Origin.AGENT);
        da.addStyleSheet((URL)null, CSSNorm.formsStyleSheet(), DOMAnalyzer.Origin.AGENT);
        da.getStyleSheets();


        BrowserCanvas contentCanvas = new BrowserCanvas(da.getRoot(), da, docSource.getURL());
        contentCanvas.setImage(new BufferedImage(700, 600 , BufferedImage.TYPE_INT_RGB));
        contentCanvas.setAutoMediaUpdate(false);
        contentCanvas.getConfig().setClipViewport(true);
        contentCanvas.getConfig().setLoadImages(true);
        contentCanvas.getConfig().setLoadBackgroundImages(true);
        contentCanvas.createLayout(windowSize);

        BufferedImage bufferedImage = contentCanvas.getImage() ;

        System.out.println(bufferedImage.getWidth());
        System.out.println(bufferedImage.getHeight());

        ImageIO.write(bufferedImage, "png", out);

        docSource.close();




        System.exit(1);*/

        /*ImageRenderer render = new ImageRenderer();
        System.out.println("kaishi");
        String url = "http://www.dinghuo.me/shop/companyGoods/companyGoodsLook.jhtml?pubType=pub_supply&id=23";
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(new File("/Users/afei/aaa.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            render.renderURL(url, out, ImageRenderer.Type.PNG);

            ImageIO.read()
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        System.out.println("OK");*/


        System.out.println(OtherTest.class.getResource("").getPath().toString());



        AssGoods[] goodsInfo = new AssGoods[11];
        AssGoods tempAssGoods = new AssGoods() ;
        tempAssGoods.setName("阿斯顿发撒发的看看看");
        goodsInfo[0] = tempAssGoods;
        goodsInfo[1] = tempAssGoods;
        goodsInfo[2] = tempAssGoods;
        goodsInfo[3] = tempAssGoods;
        goodsInfo[4] = tempAssGoods;
        goodsInfo[5] = tempAssGoods;
        goodsInfo[6] = tempAssGoods;
        goodsInfo[7] = tempAssGoods;
        goodsInfo[8] = tempAssGoods;
        goodsInfo[9] = tempAssGoods;
        goodsInfo[10] = tempAssGoods;

        //计算图片高度
        int totalHeight = 0 ;
        int sourceWidth = 720 ;
        int proportion = 1 ;
        int totalWidth = 720 /proportion;
        //proportion = (int)Math.floor((double)sourceWidth / totalWidth);
        //proportion = 1 ;
        System.out.println(proportion);
        //标题高度
        int titleHeight = 200/proportion ;

        //标题和简介之间的高度
        int intrTitleBet = 50/proportion ;
        //简介和商品之间的高度
        int intrGoodsBet = 54/proportion ;

        //商品高度
        int goodsHeight = 280/proportion;

        //商品和商品之间的高度
        int goodsGoodsBet = 45/proportion ;


        //商品和更多之间
        int goodsAndMoreBet = 81/proportion;
        //更多和名片之间的高度
        int moreAndCardBet = 85/proportion ;
        //更多高度
        int moreHeight = 120 /proportion ;
        //名片高度
        int cardHeight = 0;

        int totalIntrHeight = 0;

        //底部
        int otherTop = 30/proportion ;
        int other = 33/proportion;
        int otherBottom = 32/proportion;

        String intr = "阿斯顿发暗是谁示法阿斯顿发暗是谁示法阿斯顿发暗是谁示法阿斯顿发暗是谁示法阿斯顿发暗是谁示法阿斯顿发暗是谁示法阿斯顿发暗是谁示法阿斯顿发暗是谁示法阿斯顿发暗是谁示法阿斯顿发暗是谁示法阿斯顿发暗是谁示法阿斯顿发暗是谁示法" ;

        //处理简介
        int intrTotalWidth = totalWidth - 53/proportion - 25/proportion ;

        char[] intrChars = intr.toCharArray() ;
        int charLen = intrChars.length + 2 ;

        int everyLine = intrTotalWidth / 28 + 1 ;

        //计算数组长度
        int intrArrLen = (int)Math.ceil(((double)charLen / everyLine));
        totalIntrHeight = intrArrLen * 23 ;

        int goodsNum = goodsInfo.length ;
        int every = 3;
        //处理商品
        //判断商品最多几列
        int ceils = (int)Math.ceil((double)(goodsNum/every)) ;
        boolean needMore = false ;
        if(ceils >= 3 && goodsNum > 9){
            ceils = 3 ;
            needMore = true ;
        }
        //商品总高度
        int totalGoodsHeight = (int)(ceils*goodsHeight + (ceils-1)*goodsGoodsBet) ;

        //是否有名片
        boolean haveCard = true ;
        if(haveCard){
            cardHeight = 260/proportion ;
        }

        totalHeight = titleHeight + intrTitleBet + intrGoodsBet + goodsAndMoreBet + moreHeight + moreAndCardBet + cardHeight + otherTop + other + otherBottom + totalGoodsHeight + totalIntrHeight ;



        BufferedImage image = new BufferedImage(totalWidth, totalHeight , BufferedImage.TYPE_INT_RGB);

        Graphics2D ctx =(Graphics2D)image.getGraphics();



        ctx.setColor(Color.decode("#ffffff"));
        ctx.fillRect(0 , 0 , totalWidth , totalHeight);

        //标题
        String title = "上海会中食品有限公司";
        //标题背景图
        ctx.drawImage(ImageIO.read(new File("/Users/afei/images/haibao-bg.png")), 0, 0 , totalWidth , titleHeight ,null);
        //画标题
        Font titleFont = new Font("PingFangSC-Regular" , Font.PLAIN , 38/proportion);
        ctx.setColor(Color.decode("#FFFFFF"));
        ctx.setFont(titleFont);
        // 抗锯齿
        ctx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        FontMetrics fm = ctx.getFontMetrics(titleFont);
        FontRenderContext context = ctx.getFontRenderContext();

        Rectangle2D stringBounds = titleFont.getStringBounds(title, context);
        double fontWidth = stringBounds.getWidth();

        int titleTextWidth = fm.stringWidth(title);


        System.out.println("fontWidth:" + fontWidth);
        System.out.println("fontWidth:" + titleTextWidth);

        int titleStartX = ( totalWidth - titleTextWidth) /2;

        int tempTitleHeight = fm.getHeight();

        int currHeight = 45/proportion +  tempTitleHeight;

        ctx.drawString(title , titleStartX , currHeight);

        //主题

        String theme = "11月份画报";

        Font themeFont = new Font("PingFangSC-Regular" , Font.PLAIN , 32/proportion);
        ctx.setFont(themeFont);

        FontMetrics themeFm = ctx.getFontMetrics(themeFont);
        int themeTextWidth = themeFm.stringWidth(theme);
        int themeStartX = ( totalWidth- themeTextWidth) /2;

        currHeight += 20/proportion + themeFm.getHeight();

        ctx.drawString(theme , themeStartX , currHeight);

        currHeight+=intrTitleBet;

        //画简介
        Font intrFont = new Font("PingFangSC-Regular" , Font.PLAIN , 28/proportion);
        FontMetrics intrFm = ctx.getFontMetrics(intrFont);
        System.out.println("intr width" + intrFm.charWidth('驁'));
        System.out.println("intr height" + intrFm.getHeight());
        ctx.setFont(intrFont);
        ctx.setColor(Color.decode("#333333"));

        int left = charLen ;

        int fistStartLen = everyLine - 2 ;

        for(int i= 0 ; i< intrArrLen ; i++){

            currHeight+=20;

            int len = everyLine ;

            int start = i*everyLine ;
            int startX = 25/proportion ;

            if (intrArrLen == 1) {
                start = 0;
                len = charLen - 2;
                startX += 28 * 2;
            }

            if(intrArrLen > 1){
                if(i == 0){
                    start = 0 ;
                    len = fistStartLen;
                    startX += 28 * 2;
                }

                if(i>0 && i != (intrArrLen - 1 )){
                    start = (i-1)*everyLine +  fistStartLen + 1;
                    len = everyLine;
                }

                if(i == (intrArrLen - 1 )){
                    start = (i-1)*everyLine +  fistStartLen + 1;
                    len = charLen - 2 - ((i-1)*everyLine +  fistStartLen + 1 );
                    //int allLen = i*everyLine +  fistStartLen ;
                }
            }

            ctx.drawChars(intrChars , start , len , startX , currHeight);
            left = charLen - len;

            currHeight+=10;
            System.out.println(i);
        }


        //画商品
        currHeight+=intrGoodsBet ;

        Font goodsNameFont = new Font("PingFangSC-Regular" , Font.PLAIN , 24/proportion);
        FontMetrics goodsNameFm = ctx.getFontMetrics(goodsNameFont);

        int goodsNameFmHeight = goodsNameFm.getHeight();


        for(int i=0 ; i<ceils ; i++){

            int startKey = i * every ;

            for(int j = 0 ; j<every ; j++){

                if(startKey > (goodsNum - 1)){
                    break ;
                }
                int startWidthX = (220/proportion+ 15/proportion) * j + 15/proportion;
                if(j == 0){
                    startWidthX = 15/proportion ;
                }

                int goodsKey = startKey + j ;
                /*if(goodsKey == goodsNum){
                    goodsKey-= 1;
                }*/
                System.out.println("goods key :" + goodsKey);
                if(i == 2 && goodsKey == 8 && needMore){
                    ctx.setColor(Color.decode("#eeeeeee"));
                    ctx.drawRect(startWidthX , currHeight , 220/proportion - 1 , 280/proportion);
                    //ctx.fillRect(startWidthX , currHeight , 220/proportion - 1 , 280/proportion);

                    //画省略号

                    ctx.drawImage(ImageIO.read(new File("/Users/afei/images/diandiandian.png")) , startWidthX+((220/proportion - 52/proportion)/2) , currHeight + 60/proportion , 52/proportion,10/proportion , null);



                    Font moreGoodsFont = new Font("PingFangSC-Regular" , Font.PLAIN , 26/proportion);
                    FontMetrics moreGoodsFm = ctx.getFontMetrics(moreGoodsFont);
                    ctx.setColor(Color.decode("#333333"));
                    String moreGoodsStr = "共10件商品" ;
                    int moreGoodsWidth = moreGoodsFm.stringWidth(moreGoodsStr) ;
                    int moreGoodsHeight = moreGoodsFm.getHeight() ;

                    int moreGoodsStartX = startWidthX + (220/proportion - moreGoodsWidth)/2 ;

                    System.out.println("more goods startX:" + moreGoodsStartX);


                    ctx.drawString(moreGoodsStr , moreGoodsStartX  , currHeight + 148/proportion  );

                    break ;
                }



                AssGoods goods = goodsInfo[goodsKey] ;

                ctx.drawImage(ImageIO.read(new File("/Users/afei/fuck.jpg")), startWidthX , currHeight , 220/proportion, 220/proportion , null);

                ctx.setColor(Color.decode("#eeeeee"));
                ctx.drawRect(startWidthX , currHeight +220/proportion , 220/proportion - 1 , 60/proportion);
                ctx.fillRect(startWidthX , currHeight +220/proportion , 220/proportion - 1 , 60/proportion);

                String goodsName = goods.getName() ;

                if (goodsName.length()>9){
                    goodsName = goodsName.substring(0,8) + "...";
                }

                //位置
                int goodsNameX = 10/proportion + startWidthX;
                int goodsNameY = currHeight +220/proportion  + goodsNameFmHeight;

                ctx.setFont(goodsNameFont);
                ctx.setColor(Color.decode("#333333"));
                ctx.drawString(goodsName , goodsNameX , goodsNameY);

            }

            currHeight+=280/proportion;

            if(i != 2){
                currHeight+=45/proportion;
            }

        }

        //画more

        String moreStr = "更多商品信息" ;
        String pleaseStr = "请长按识别小程序码" ;
        Font moreFont = new Font("PingFangSC-Regular" , Font.PLAIN , 28/proportion);
        FontMetrics moreFm = ctx.getFontMetrics(moreFont) ;
        int moreTextHeight = moreFm.getHeight();
        //currHeight+=moreTextHeight ;

        ctx.drawString(moreStr , 94/proportion , currHeight + 81/proportion + moreTextHeight);
        ctx.drawString(pleaseStr , 94/proportion , currHeight+ 81/proportion+ 120/proportion);

        //画小程序
        ctx.drawImage(ImageIO.read(new File("/Users/afei/images/two.jpg")) , 435/proportion , currHeight + 60/proportion , 160/proportion,160/proportion , null);

        currHeight+=81/proportion+120/proportion+85/proportion ;

        //画名片
        if(haveCard){
            ctx.setColor(Color.decode("#fafafa"));
            ctx.drawRect(0 , currHeight, totalWidth , 260/proportion);
            ctx.fillRect(0 , currHeight , totalWidth , 260/proportion);

            //画other
            String cardName = "王超" ;
            String cardPosition = "销售经理" ;
            String wx = "asdfasdfaf_a";
            String tel = "13512565697" ;

            Font cardNameFont = new Font("PingFangSC-Regular" , Font.BOLD , 34/proportion);
            FontMetrics cardNameFm = ctx.getFontMetrics(cardNameFont);
            int cardNameH = cardNameFm.getHeight() ;
            int cardNameW = cardNameFm.stringWidth(cardName) ;
            ctx.setFont(cardNameFont);
            ctx.setColor(Color.decode("#333333"));
            ctx.drawString(cardName , 50/proportion , currHeight + 40/proportion + cardNameH);

            Font cardPositionFont = new Font("PingFangSC-Regular" , Font.PLAIN , 28/proportion);
            FontMetrics cardPositionFm = ctx.getFontMetrics(cardPositionFont);
            int cardPositionH = cardPositionFm.getHeight() ;
            ctx.setFont(cardPositionFont);
            ctx.drawString(cardPosition , 50/proportion + cardNameW + 36/proportion, currHeight + 50/proportion + cardPositionH);

            ctx.drawImage(ImageIO.read(new File("/Users/afei/images/weixin.png")) , 78/proportion , currHeight + 127/proportion , 32/proportion,26/proportion , null);

            ctx.setFont(cardPositionFont);
            ctx.drawString(wx , 154/proportion, currHeight + 120/proportion + cardPositionH -3 );


            ctx.drawImage(ImageIO.read(new File("/Users/afei/images/shouji.png")) , 84/proportion , currHeight + 180/proportion , 21/proportion,30/proportion , null);

            ctx.setFont(cardPositionFont);
            ctx.drawString(tel , 154/proportion, currHeight + 175/proportion + cardPositionH -3);

            ctx.drawImage(ImageIO.read(new File("/Users/afei/images/qinglianxi.png")) , 410/proportion , currHeight + 80/proportion , 270/proportion,110/proportion , null);

            currHeight+=260/proportion ;
        }

        //
        String bottom = "一 快来生成属于您的订货助理 一";
        Font bottomFont = new Font("PingFangSC-Regular" , Font.PLAIN , 24/proportion);
        FontMetrics bottomFm = ctx.getFontMetrics(bottomFont);

        int bottomX = bottomFm.stringWidth(bottom) ;
        int bottomY = bottomFm.getHeight() ;
        int bottomStartX = (totalWidth - bottomX) / 2 ;
        ctx.setFont(bottomFont);
        bottomFm.getAscent() ;
        bottomFm.getDescent() ;

        ctx.drawString(bottom , bottomStartX , currHeight + 30/proportion + bottomY);

        ctx.dispose();

        try {
            ImageIO.write(image , "jpg" , new File("/Users/afei/aaa.jpg")) ;
        } catch (IOException e) {
            e.printStackTrace();
        }




        /*AssGoods[] goodsInfo = new AssGoods[9];
        AssGoods tempAssGoods = new AssGoods() ;
        tempAssGoods.setName("阿斯顿发撒发的");
        goodsInfo[0] = tempAssGoods;
        goodsInfo[1] = tempAssGoods;
        goodsInfo[2] = tempAssGoods;
        goodsInfo[3] = tempAssGoods;
        goodsInfo[4] = tempAssGoods;
        goodsInfo[5] = tempAssGoods;
        goodsInfo[6] = tempAssGoods;
        goodsInfo[7] = tempAssGoods;

        boolean cardBool = false ;

        double widthX = 1074d;
        double goodsWidth = (widthX - 80) / 3;

        int goodsSum = goodsInfo.length ;

        double textWidth = widthX - 40;
        double textNum = Math.floor(textWidth / 16);//一行多少个文字

        double height = 110d;
        String text = "阿瑟费撒地方阿斯顿发上的发烧发烧发烧发大发两块即可立即离开大煞风景；看见阿斯顿发开阿瑟费撒地方阿斯顿发上的发烧发烧发烧发大发两块即可立即离开大煞风景；看见阿斯顿发开" ;

        //简介
        //文字的总高度
        if (StringUtils.isNotEmpty(text)) {
            height = 110 + 40;
            double textLineNum = Math.ceil(text.length() / textNum);
            height = height + (textLineNum - 1) * 30 + 30;
            //console.log(height);
        }
        //商品的总高度
        double goodsHeight = height;
        if (goodsSum > 0) {
            double goodsHNum = Math.ceil(goodsSum / 3);
            goodsHeight = height + (goodsWidth + 30 + 20) * goodsHNum - 20;
            height = goodsHeight;
        }
        //console.log(180 + "--height--" + height);
        height = height + 140;
        //console.log(182 + "--height--" + height);
        if (cardBool){
            height = height + 130;
        }
        height += 50;
        double canvasHeight = height * 2+80 ;
        int Y = 0 ;
        //console.log(197 +"--Y--"+Y);
        BufferedImage image = new BufferedImage((int)widthX, (int)canvasHeight, BufferedImage.TYPE_INT_RGB);

        Graphics2D ctx =(Graphics2D)image.getGraphics();



        //ctx.setColor('#eeeeee');
        ctx.setColor(Color.decode("#eeeeee"));
        ctx.fillRect(0, 0, (int)widthX, (int)height + 50);



        //ctx.setFillStyle('#ffffff');
        ctx.setColor(Color.decode("#ffffff"));
        Y = 5;
        widthX = widthX - 10;
        ctx.fillRect(5, Y, (int)widthX, (int)height - 6);

        // 标题
        //ctx.setFillStyle('#4DA1ff');
        ctx.fillRect(5, Y, (int)widthX, 110);

        //ctx.drawImage("/images/no-shangpin.png", 20, 100, 100, 100)
        ctx.drawImage(ImageIO.read(new File("/Users/afei/images/haibao-bg.png")), 5, Y , (int)widthX, 110 ,null);
        //title
        Font fontTitle = new Font("Serif" , Font.BOLD , 20);
        ctx.setFont(fontTitle);
        ctx.setColor(Color.decode("#ffffff"));
        int centerX = (int) (widthX /proportion) ;

        //ctx.setFillStyle('#ffffff');
        //ctx.setTextAlign('center');
        Y = Y + 45;
        //ctx.fillText(title, centerX, Y)//55

        ctx.drawString("中国你好" , centerX , Y);

        //theme
        Font themeTitle = new Font("Serif" , Font.BOLD , 18);
        ctx.setFont(themeTitle);
        //ctx.setFontSize(18);
        Y = Y + 35;
        ctx.drawString("在重中之重重中之重", centerX, Y);
        //textcom
        //int textNum = Math.floor(textWidth / 16);//一行多少个文字

        *//*ctx.setFillStyle('#333333');
        ctx.setFontSize(16);
        ctx.setTextAlign('left');*//*

        Font textFont = new Font("微软雅黑" , Font.BOLD , 16);
        ctx.setFont(textFont);
        ctx.setColor(Color.decode("#ffffff"));

        int lastSubength = 0, index = 0;

        Y = 110 ;

        if(text.length() > 0){
            Y = 150;
        }
        double firstNum = textNum;
        String firstLine = text.substring(lastSubength, (int)firstNum - 2);//第一行文字空两格
        String lastText = text.substring(firstLine.length(), text.length());
        ctx.drawString(firstLine, 50, Y);

        for (int i = 1; i <= lastText.length(); i++) {
            if ((lastText.length() - index * textNum) < textNum) {
                Y = Y + 30;
                ctx.drawString(lastText.substring(lastSubength, lastText.length()), 20, Y);
                break;
            }
            if (i % textNum == 0) {
                Y = Y + 30;
                ctx.drawString(lastText.substring(lastSubength, i), 20, Y);
                index = index + 1;
                lastSubength = i;
            }
        }

        //商品信息
        Y = Y + 30;
        //double goodsWidth = (widthX - 60) / 3;
        double goodsX = 25, goodsY = Y;
        for (int i = 0; i < goodsSum; i++) {
            goodsX = 25;
            goodsY = Y;
            if (i < 3) {
                goodsX = goodsX + (goodsWidth + 10) * i;
            } else if (i >= 3 && i < 6) {
                int j = i - 3;
                goodsX = goodsX + (goodsWidth + 10) * j;
                goodsY = goodsY + (goodsWidth + 30 + 15);
            } else if (i >= 6 && i < 8) {
                int j = i - 6;
                goodsX = goodsX + (goodsWidth + 10) * j;
                goodsY = goodsY + (goodsWidth + 30 + 15) * 2;
            } else if (i == 8) {
                goodsX = goodsX + (goodsWidth + 10) * 2;
                goodsY = goodsY + (goodsWidth + 30 + 15) * 2;

                ctx.setColor(Color.decode("#ffffff"));
                //ctx.setFillStyle('#ffffff');
                *//*ctx.beginPath();
                ctx.setLineWidth(0.2);
                ctx.moveTo(goodsX, goodsY);
                ctx.rect(goodsX, goodsY, goodsWidth, goodsWidth + 30);
                ctx.stroke();*//*
                ctx.drawRect((int)goodsX, (int)goodsY, (int)goodsWidth, (int)goodsWidth + 30);

                ctx.drawImage(ImageIO.read(new File("/Users/afei/images/no-shangpin.png")), (int)(goodsX + (goodsWidth - 30) /proportion), (int)goodsY + 40, 30, 30 , null);

                *//*ctx.setFillStyle('#333333');
                ctx.setTextAlign('center');
                *//*

                Font goodsFont = new Font("微软雅黑" , Font.BOLD , 16);
                ctx.setFont(goodsFont);
                ctx.setColor(Color.decode("#333333"));

                ctx.drawString("共10件商品", (int)(goodsX + (goodsWidth /proportion)), (int)goodsY + 90);

                //ctx.setFillStyle('#333333');
                break;
            }

            *//*ctx.setFillStyle('#fbfbfb');
            ctx.beginPath();
            ctx.setLineWidth(0.2);
            ctx.moveTo(goodsX, goodsY);
            ctx.rect(goodsX, goodsY, goodsWidth, goodsWidth + 30);
            ctx.stroke();*//*
            ctx.setColor(Color.decode("#fbfbfb"));

            //ctx.setFillStyle('#eeeeee');
            //ctx.setComposite(AlphaComposite.Src);
            //ctx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            //ctx.setColor(Color.BLACK);

            //ctx.setComposite(AlphaComposite.SrcAtop);

            //ctx.fill(new RoundRectangle2D.Float((int)goodsX, (int)goodsY, (int)goodsWidth, (int)goodsWidth, 100 , 100));

            //ctx.drawRoundRect((int)(goodsX + 0.2), (int)(goodsY + goodsWidth + 0.2),(int) (goodsWidth - 0.3), (int)(30 - 0.3) , 150 , 100);

            ctx.drawImage(ImageIO.read(new File("/Users/afei/fuck.jpg")), (int)goodsX, (int)goodsY, (int)goodsWidth-10, (int)goodsWidth-10 , null);

            Font goodsFont1= new Font("微软雅黑" , Font.BOLD , 10);
            //ctx.setFontSize(10);
            //ctx.setFillStyle('#333333');
            ctx.setColor(Color.decode("333333"));
            ctx.setFont(goodsFont1);
            //ctx.setTextAlign('center');
            String goodsName = goodsInfo[i].getName() ;
            if (goodsName.length()>9){
                goodsName = goodsName.substring(0,8);
                goodsName +="..." ;
            }
            ctx.drawString(goodsName, (int)(goodsX + goodsWidth/proportion), (int)(goodsY + goodsWidth + 20));
        }

        if (goodsSum > 0) {
            Y = (int)(goodsY + goodsWidth + 30);
        }
        //商品的总高度
        Y = (int)goodsHeight;

        Font otherFont = new Font("微软雅黑" , Font.BOLD , 14);

        //ctx.setTextAlign('left');
        //ctx.setFontSize(14);
        ctx.setFont(otherFont);
        ctx.drawString("更多商品信息", 40, Y + 60);
        ctx.drawString("请长按识别小程序码", 40, Y + 90);
        ctx.drawImage(ImageIO.read(new File("/Users/afei/hello.png")), 240, Y + 30, 80, 80 , null);

        Y = Y + 140;

        *//*if (_this.data.cardBool == 'true') {
            var lineX = -10, lineY = Y, lineNum = 0;
            lineNum = Math.round(widthX / 15);
            for (var i = 0; i < lineNum; i++) {
                lineX = lineX + 15;
                ctx.beginPath();
                ctx.setLineWidth(0.2);
                ctx.moveTo(lineX, lineY);
                ctx.lineTo(lineX + 8, lineY);
                ctx.stroke();
                if (i == lineNum - 1) {
                    if (lineX + 8 < widthX + 5) {
                        ctx.beginPath();
                        ctx.setLineWidth(0.2);
                        ctx.moveTo(lineX + 12, lineY);
                        ctx.lineTo(widthX + 5, lineY);
                        ctx.stroke();
                    }
                }
            }

            ctx.setFillStyle('#fbfbfb');
            ctx.fillRect(5, Y, widthX, 130);
            ctx.setFillStyle('#333333');
            ctx.setFontSize(18);
            ctx.fillText(dataInfo.name, 40, Y + 40);
            if (dataInfo.position != 'null' && dataInfo.position != null){
                ctx.setFontSize(14);
                ctx.fillText(dataInfo.position, 130, Y + 40);
            }


            ctx.drawImage("/images/weixin.png", 40, Y + 60, 18, 18);
            ctx.fillText(dataInfo.wxNum, 70, Y + 74);

            ctx.drawImage("/images/shouji.png", 40, Y + 90, 18, 18);
            ctx.fillText(dataInfo.phone, 70, Y + 105);

            ctx.drawImage("/images/qinglianxi.png", 210, Y + 40, 120, 60);
            Y = Y + 130;
        }
*//*
        ctx.setColor(Color.decode("#eeeeee"));
        //ctx.setFillStyle('#eeeeee');
        ctx.fillRect(5, Y, (int)widthX, 50);

        Font otherFont1 = new Font("微软雅黑" , Font.BOLD , 12);
        ctx.setFont(otherFont1);

        //ctx.setFontSize(12);
        //ctx.setTextAlign('center');
        //ctx.setFillStyle('#bbbbbb');
        ctx.setColor(Color.decode("#bbbbbb"));
        ctx.drawString("— 快来生成属于您的订货助理 —", (int)widthX/proportion, Y + 25);

       *//* Y = Y + 50;
        console.log(Y);
        //ctx.fillText('', 180, 100)
        ctx.draw();*//*



        ctx.dispose();

        try {
            ImageIO.write(image , "jpg" , new File("/Users/afei/aaa.jpg")) ;
        } catch (IOException e) {
            e.printStackTrace();
        }



*/

        /*ImageRenderer render = new ImageRenderer();
        System.out.println("kaishi");
        String url = "http://www.dinghuo.me/shop/companyGoods/companyGoodsLook.jhtml?pubType=pub_supply&id=23";
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(new File("/Users/afei/aaa.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            render.renderURL(url, out, ImageRenderer.Type.PNG);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        System.out.println("OK");*/


        /*JEditorPane ed = null;
        try {
            ed = new JEditorPane(new URL("http://www.dinghuo.me/shop/companyGoods/companyGoodsLook.jhtml?pubType=pub_supply&id=23"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("10");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ed.setSize(1000,1000);

        //create a new image
        BufferedImage image = new BufferedImage(ed.getWidth(), ed.getHeight(),
                BufferedImage.TYPE_INT_ARGB);

        //paint the editor onto the image
        SwingUtilities.paintComponent(image.createGraphics(),
                ed,
                new JPanel(),
                0, 0, image.getWidth(), image.getHeight());
        //save the image to file
        try {
            ImageIO.write((RenderedImage)image, "png", new File("/Users/afei/aaa.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("ok");


        System.exit(1);





        ImageRenderer render = new ImageRenderer();
        System.out.println("kaishi");
        String url = "http://www.dinghuo.me/shop/companyGoods/companyGoodsLook.jhtml?pubType=pub_supply&id=23";
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(new File("/Users/afei/aaa.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            render.renderURL(url, out, ImageRenderer.Type.PNG);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        System.out.println("OK");



        System.exit(1);*/




/*
        BufferedImage image = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_RGB);

        Graphics g = image.getGraphics();

        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, 1000, 1000);
        g.setColor(Color.BLACK);
        Font font = new Font("宋体" , Font.BOLD , 100);
        g.setFont(font);
        g.drawString("中国你好中国你好", 0, 100);
        g.drawString("中国你好中国你好", 0, 200);


        g.drawRect(200 , 200 , 200 , 200);
        //g.setColor(Color.red);
        g.fillRect(200 , 200 , 200 , 200);

        g.getFontMetrics();
        g.dispose();

        try {
            ImageIO.write(image , "jpg" , new File("/Users/afei/aaa.jpg")) ;
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        System.exit(1);
        int aaa = 0x123 ;

        List<String> testList = new ArrayList<String>(){{
            this.add("1");
            this.add("1");
            this.add("1");
            this.add("1");
            this.add("11");
            this.add("1");
            this.add("1");
            this.add("1");
        }};



        for (String strTT :testList){
            if(strTT.equals("11")){
               testList.remove(strTT) ;
            }
        }




        System.exit(1);

        String[] orders = new String[]{"1"};
        int orderLen = orders.length ;
        StringBuffer snSb = new StringBuffer();
        for(int i= 0 ; i< orderLen ; i++){
            if(i > 0){
                snSb.append("、") ;
            }
            snSb.append(orders[i]) ;
        }

        System.out.println(snSb.toString());



        Sn sn = new Sn() ;

        sn.setLastValue(9L);

        Long last = sn.getLastValue() ;

        sn.setLastValue(8L);


        Long last2 = sn.getLastValue() ;

        System.out.println(last);

        System.out.println(last2);

    }
}
