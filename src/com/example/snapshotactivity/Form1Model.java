package com.example.snapshotactivity;

import java.io.Serializable;

public class Form1Model implements Serializable {
	public String anhao;//案号
	public String xfa;//_罚
	public String qiangcuo;//强措
	public String xhao;//_号
	public String dName;//公民姓名
	public String dIdCard;//公民身份证号
	public String dAdress;//公民住址
	public String dPhone;//公民联系电话
	public String fName;//法人名称
	public String fAdress;//法人住址
	public String fPhone;//法人联系电话
	public String fDPersion;//法定代表人
	
	public String getAnhao() {
		return anhao;
	}
	public void setAnhao(String anhao) {
		this.anhao = anhao;
	}
	public String getXfa() {
		return xfa;
	}
	public void setXfa(String xfa) {
		this.xfa = xfa;
	}
	public String getQiangcuo() {
		return qiangcuo;
	}
	public void setQiangcuo(String qiangcuo) {
		this.qiangcuo = qiangcuo;
	}
	public String getXhao() {
		return xhao;
	}
	public void setXhao(String xhao) {
		this.xhao = xhao;
	}
	public String getdName() {
		return dName;
	}
	public void setdName(String dName) {
		this.dName = dName;
	}
	public String getdIdCard() {
		return dIdCard;
	}
	public void setdIdCard(String dIdCard) {
		this.dIdCard = dIdCard;
	}
	public String getdAdress() {
		return dAdress;
	}
	public void setdAdress(String dAdress) {
		this.dAdress = dAdress;
	}
	public String getdPhone() {
		return dPhone;
	}
	public void setdPhone(String dPhone) {
		this.dPhone = dPhone;
	}
	public String getfName() {
		return fName;
	}
	public void setfName(String fName) {
		this.fName = fName;
	}
	public String getfAdress() {
		return fAdress;
	}
	public void setfAdress(String fAdress) {
		this.fAdress = fAdress;
	}
	public String getfPhone() {
		return fPhone;
	}
	public void setfPhone(String fPhone) {
		this.fPhone = fPhone;
	}
	public String getfDPersion() {
		return fDPersion;
	}
	public void setfDPersion(String fDPersion) {
		this.fDPersion = fDPersion;
	}
}
