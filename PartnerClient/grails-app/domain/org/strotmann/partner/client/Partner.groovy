package org.strotmann.partner.client

import grails.plugins.rest.client.RestBuilder
import grails.plugins.rest.client.RestResponse
import grails.util.Holders

import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject

class Partner {

	String name

	String toString() { name }

	static List<Partner> getPartners() {
		List<Partner> partnerList = []
		RestResponse resp = getUri('?max=1000')
		if (resp) {
			for (JSONObject jsonObject in new JSONArray(resp.text)) {
				 if (jsonObject.getString("class") == 'org.strotmann.partner.Organisation') {
					 partnerList << createPartner(jsonObject)
				 }
				 else {
					 partnerList << new Partner()
				 }
			}
		}
		partnerList.sort{it.name}
	}

	static Partner getPartner(long partnerId) {
		RestResponse resp = getUri("/get?id=$partnerId")
		if (resp) {
			return createPartner(new JSONArray(resp.text).getJSONObject(0))
		}
	}

	static Partner getPartner(String rolle, String objektname, long objektId) {
		RestResponse resp = getUri("/getViaParo?rolle=$rolle&objektname=$objektname&objektId=$objektId")
		if (resp) {
			return createPartner(new JSONArray(resp.text).getJSONObject(0))
		}
	}

	static String getPartnerUri() {
		Holders.config.partnerService
	}

	static boolean savePartnerrolle(long partnerId, long oldId, String rolle, String objektname, long objektId) {
		getUri("/saveRolle?id=$partnerId&oldId=$oldId&rolle=$rolle&objektname=$objektname&objektId=$objektId")?.text == 'ok'
	}

	static boolean loePartnerrolle(String rolle, String objektname, long objektId) {
		getUri("/loeRolle?rolle=$rolle&objektname=$objektname&objektId=$objektId")?.text == 'ok'
	}

	//speichert die Uri einer Fremdanwendung
	static boolean saveRueckUri(String anwendung, String uri) {
		getUri("/saveRueckUri?anwendung=$anwendung&uri=$uri")?.text == 'ok'
	}

	private static RestResponse getUri(String uri) {
		new RestBuilder().get "$partnerUri/partner$uri"
	}

	private static Partner createPartner(JSONObject jsonObject) {
		Partner p = new Partner()
		p.id = jsonObject.getInt("id")
		p.name = jsonObject.getString("name")
		p
	}
}
