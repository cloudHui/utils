package utils.utils.test;

public class StringChange {

	public static void main(String[] args){
		String s = "常 用 的 第 三 方 模 块 龙 openpyxl 模 块 是 用 于 处 理 Microsoft Exce | 文 件 的 第 三 方 库 openpyxl 龙 可 以 对 Exce 反 件 中 的 数 据 进 行 写 入 和 读 取 函 数 / 属 性 名 称 load workbook(filename) workbook.sheetnames sheet.append(lst) workbook.save(excelname) Wo rkbook() 功 能 描 述 打 开 已 存 在 的 表 格 ， 结 果 为 工 作 簿 对 象 工 作 簿 对 象 的 sheetnames 属 性 ， 用 于 获 取 所 有 工 作 表 的 名 称 ， 结 果 为 列 表 类 型 向 工 作 表 中 添 加 一 行 数 据 ， 新 数 据 接 在 工 作 表 已 有 数 据 的 后 面 保 存 工 亻 乍 簿 创 建 新 的 工 作 簿 对 象";


		s= s.replace(" ","");
		s= s.replace("，",",");
		System.out.println(s);
	}
}
