package org.strotmann.partner.client

import java.util.Map;

import grails.plugins.rest.client.*
import grails.util.Holders

import org.codehaus.groovy.grails.web.json.JSONObject
import org.codehaus.groovy.grails.web.json.JSONArray

class Partner {
	
	String name
			
	String toString() {"${this.name}"}
	
	static List getPartners() {
		List <Partner> partnerList = []
		def RestBuilder rest = new RestBuilder()
		RestResponse resp
		resp = rest.get("${Holders.config.partnerService}/partner?max=1000")
		if (resp) {	
			JSONArray array = new JSONArray(resp.text)
			for (int i=0;i<array.length();i++) {
				 Partner p = new Partner()
				 JSONObject jsonObject = array.getJSONObject(i)
				 if (jsonObject.getString("class") == 'org.strotmann.partner.Organisation') {
					 p.id = jsonObject.getInt("id")
					 p.name = jsonObject.getString("name") 
					 partnerList << p
				 }
			}
		}
		partnerList.sort{it.name}
	}
	
	static Partner getPartner(long partnerId) {
		def RestBuilder rest = new RestBuilder()
		RestResponse resp
		resp = rest.get("${Holders.config.partnerService}/partner/get?id=${partnerId.toString()}")
		Partner p
		if (resp) {
			JSONArray array = new JSONArray(resp.text)
			p = new Partner()
			JSONObject jsonObject = array.getJSONObject(0)
			p.id = jsonObject.getInt("id")
			p.name = jsonObject.getString("name")
		}
		p
	}
	
	static Partner getPartner(String rolle, String objektname, long objektId) {
		def RestBuilder rest = new RestBuilder()
		RestResponse resp
		resp = rest.get("${Holders.config.partnerService}/partner/getViaParo?rolle=${rolle}&objektname=${objektname}&objektId=${objektId.toString()}")
		Partner p
		if (resp) {
			JSONArray array = new JSONArray(resp.text)
			p = new Partner()
			JSONObject jsonObject = array.getJSONObject(0)
			p.id = jsonObject.getInt("id")
			p.name = jsonObject.getString("name")
		}
		p
	}
	
	static String getPartnerUri() {
		"${Holders.config.partnerService}"
	}
	
	static Boolean savePartnerrolle(long partnerId, long oldId, String rolle, String objektname, long objektId) {
		boolean retV = false
		def RestBuilder rest = new RestBuilder()
		RestResponse resp
		String s = "${Holders.config.partnerService}/partner/saveRolle?id=${partnerId.toString()}&oldId=${oldId}&rolle=${rolle}&objektname=${objektname}&objektId=${objektId}"
		resp = rest.get(s)
		if (resp) {
			if (resp.text == 'ok')
				retV = true
		}
		retV
	}
	
	static Boolean loePartnerrolle(String rolle, String objektname, long objektId) {
		boolean retV = false
		def RestBuilder rest = new RestBuilder()
		RestResponse resp
		String s = "${Holders.config.partnerService}/partner/loeRolle?rolle=${rolle}&objektname=${objektname}&objektId=${objektId}"
		resp = rest.get(s)
		if (resp) {
			if (resp.text == 'ok')
				retV = true
		}
		retV
	}
	
	static Boolean saveRueckUri(String anwendung, String uri) {
		boolean retV = false
		def RestBuilder rest = new RestBuilder()
		RestResponse resp
		String s = "${Holders.config.partnerService}/partner/saveRueckUri?anwendung=${anwendung}&uri=${uri}"
		resp = rest.get(s)
		if (resp) {
			if (resp.text == 'ok')
				retV = true
		}
		retV
	}
}
