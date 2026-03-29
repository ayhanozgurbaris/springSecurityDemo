package com.security.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class RedisTestService {

    // @Cacheable: Bu metodun sonucunu "products" adıyla Redis'e kaydet.
    // Bir daha aynı "id" ile istek gelirse bu metodun İÇİNE HİÇ GİRME, direkt Redis'ten ver!
    @Cacheable(value = "products", key = "#id")
    public String getProductById(String id) {
        System.out.println("🚨 DİKKAT: Veritabanına iniliyor... Çok ağır bir işlem yapılıyor!");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return id + " numaralı harika ürünün detayları (Veritabanından geldi)";
    }
}
