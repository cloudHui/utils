package utils.other.excel;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.mysql.cj.util.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.imageio.ImageIO;

public class WritePictureExcel {


	public static void main(String[] args) {
	}


	private void addPicture(String imgUrl, HSSFSheet sheet, int index, HSSFWorkbook wb, int width) {
		if (!StringUtils.isNullOrEmpty(imgUrl)) {
			HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
			BufferedImage bufferImg = null;
			//网络图片用下面的方法处理不适合本地图片
			URL url = null;
			try {
				url = new URL(imgUrl);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
			if (url != null) {
				try {
					bufferImg = ImageIO.read(url);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (bufferImg != null) {
				try {
					String type = imgUrl.substring(imgUrl.lastIndexOf(".") + 1);
					int typeInt = HSSFWorkbook.PICTURE_TYPE_JPEG;
					if ("png".equals(type)) {
						typeInt = HSSFWorkbook.PICTURE_TYPE_PNG;
					}
					ImageIO.write(bufferImg, type, byteArrayOut);
					// width 第几列
					HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0,
							(short) (width), (1 + index), (short) (width + 1), (2 + index));
					patriarch.createPicture(anchor, wb.addPicture(byteArrayOut.toByteArray(), typeInt));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
