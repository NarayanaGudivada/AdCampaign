package org.example.ws.web.api;

import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.example.ws.model.Advertising;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdcampaignController {

	private static BigInteger partnerId;
	private static Map<BigInteger, Advertising> advertingMap = new HashMap<BigInteger, Advertising>();

	private static Advertising save(Advertising advertising) {
		if (advertingMap.isEmpty()) {
//			advertingMap = new HashMap<BigInteger, Advertising>();
			partnerId = BigInteger.ONE;
		}
		if (advertising.getPartnerId() != null) {
			if (advertingMap.containsKey(advertising.getPartnerId())) {
				Advertising ad = advertingMap.get(advertising.getPartnerId());
				if (!ad.getExpiresIn().equalsIgnoreCase("Already Expired")) {
					ad.setStatusCode(HttpStatus.FORBIDDEN);
					ad.setStatusMessage("Duplicate Ad. Given Parter alrady have an active Ad");
					return ad;
				} else {
					advertingMap.remove(ad);
					advertingMap.put(advertising.getPartnerId(), advertising);
					return advertising;

				}
			} else {
				advertingMap.put(advertising.getPartnerId(), advertising);
				return advertising;
			}
		}

		while (advertingMap.containsKey(partnerId)) {
			partnerId = partnerId.add(BigInteger.ONE);
		}
		advertising.setPartnerId(partnerId);
		partnerId = partnerId.add(BigInteger.ONE);
		advertingMap.put(advertising.getPartnerId(), advertising);
		return advertising;
	}

//	static {
//		Advertising ad1 = new Advertising();
//		ad1.setDuration(60);
//		ad1.setAdContent("Hellow World!. This is your Ad1");
//		save(ad1);
//
//		Advertising ad2 = new Advertising();
//		ad2.setDuration(120);
//		ad2.setAdContent("Hellow World!. This is your Ad2");
//		save(ad2);
//
//	}

	@RequestMapping(
			value = "/api/advertisings",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<Advertising>> getAdvertisings() {
		if(!advertingMap.isEmpty()){
			advertingMap.forEach((a, b) -> b.setStatusCode(null));
			advertingMap.forEach((a, b) -> b.setStatusMessage(null));
			Collection<Advertising> advertisings = advertingMap.values();
			return new ResponseEntity<Collection<Advertising>>(advertisings, HttpStatus.OK);
		}else{
			return new ResponseEntity<Collection<Advertising>>(HttpStatus.NO_CONTENT);
		}

	}

	@RequestMapping(
			value = "/api/advertisings/{partnerId}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Advertising> getAdvertising(@PathVariable("partnerId") BigInteger partnerId) {
		Advertising ad = advertingMap.get(partnerId);
		if (ad == null) {
			ad = new Advertising();
			ad.setStatusCode(HttpStatus.NOT_FOUND);
			ad.setStatusMessage("No active ad campaigns exist for the specified partner");
			return new ResponseEntity<Advertising>(ad,HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Advertising>(ad, HttpStatus.OK);
	}
	
	
	
	@RequestMapping(
			value = "/api/advertisings",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Advertising> createAvertising(@RequestBody Advertising advertising){
		Advertising ad = save(advertising);
		if(ad.getStatusCode() != null){
			return new ResponseEntity<Advertising>(ad, HttpStatus.FORBIDDEN);
		}
		return new ResponseEntity<Advertising>(ad, HttpStatus.CREATED);
	}

}
