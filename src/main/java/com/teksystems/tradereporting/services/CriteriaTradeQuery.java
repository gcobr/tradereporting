package com.teksystems.tradereporting.services;

import com.teksystems.tradereporting.model.TradeEvent;
import com.teksystems.tradereporting.model.TradeEventId;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CriteriaTradeQuery implements TradeQuery {

    private final EntityManager entityManager;

    public CriteriaTradeQuery(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<TradeEvent> getTrades() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TradeEvent> cq = cb.createQuery(TradeEvent.class);
        Root<TradeEvent> root = cq.from(TradeEvent.class);
        Path<TradeEventId> tradeEventId = root.get("tradeEventId");
        cq.where(
                cb.or(
                        cb.and(
                                cb.equal(tradeEventId.get("sellerParty"), "EMU_BANK"),
                                cb.equal(tradeEventId.get("premiumCurrency"), "AUD")
                        ),
                        cb.and(
                                cb.equal(tradeEventId.get("sellerParty"), "BISON_BANK"),
                                cb.equal(tradeEventId.get("premiumCurrency"), "USD")
                        )
                )
        );
        return entityManager.createQuery(cq).getResultList();
    }

}
