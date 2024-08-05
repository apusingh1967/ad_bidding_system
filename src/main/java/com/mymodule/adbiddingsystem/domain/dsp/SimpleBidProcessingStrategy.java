package com.mymodule.adbiddingsystem.domain.dsp;

import com.mymodule.adbiddingsystem.common.messages.Bid;
import com.mymodule.adbiddingsystem.common.messages.BidRequest;
import com.mymodule.adbiddingsystem.common.config.ConfigObject;
import com.mymodule.adbiddingsystem.common.messages.Dimension;

import java.math.BigDecimal;
import java.util.*;

public class SimpleBidProcessingStrategy implements BidProcessingStrategy {
    @Override
    public Optional<Bid> processBidRequest(BidRequest bidRequest, List<Campaign> campaigns, DspActor dsp) {
        Campaign campaign = null;
        for(var candidateCampaign: campaigns){
            var selected = compareCountry(bidRequest, candidateCampaign)
            && compareDomain(bidRequest.url(), candidateCampaign.targetedDomain());
            var selectedDimensions = new ArrayList<Dimension>();
            for(var dimension: candidateCampaign.dimensions()){
                if(bidRequest.dimension().contains(dimension)){
                    selectedDimensions.add(dimension);
                }
            }
            if(selected && selectedDimensions.size() > 0) {
                campaign = new Campaign(candidateCampaign.id(), candidateCampaign.targetedCountry(),
                        candidateCampaign.targetedDomain(), selectedDimensions);
            }
        }
        if(campaign == null) return Optional.empty();
        return Optional.of(new Bid(bidRequest.requestId(), getRandomQuote(), campaign, bidRequest, dsp));
    }

    private boolean compareCountry(BidRequest bidRequest, Campaign candidateCampaign) {
        return bidRequest.country().equalsIgnoreCase(candidateCampaign.targetedCountry());
    }

    // use Levenshtein Distance to find how much match is there of targeted domain inside the url
    private boolean compareDomain(String url, String domain){
        var cnt = 0;
        for(int i = 0, j = 0; i < domain.length() && j < url.length();){
            char c1 = domain.charAt(i);
            char c2 = url.charAt(j);
            if(c1 == c2) {
                cnt++;
                i++;
                j++;
            } else {
                j++;
            }
        }
        var perc = cnt * 1D / domain.length();
        if(perc > ConfigObject.instance.LEVENSHTEIN_DISTANCE_THRESHOLD) return true;
        else return false;
    }

    private Random random = new Random();
    private BigDecimal getRandomQuote(){
        var val = random.nextGaussian(1D, 0.1);
        return new BigDecimal(String.format("%.2f", val));
    }
}
