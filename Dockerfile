# ==========================================
# 1. AŞAMA: İNŞAAT (Builder)
# Maven yüklü bir Linux ortamı alıyoruz
# ==========================================
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

# Sadece pom.xml ve kaynak kodları (src) kopyalıyoruz (target klasörü dışarıda kalıyor!)
COPY pom.xml .
COPY src ./src

# Projeyi Linux'un kendi içinde, taptaze bir şekilde derliyoruz
RUN mvn clean package -DskipTests

# ==========================================
# 2. AŞAMA: ÇALIŞTIRMA (Runner)
# Sadece Java yüklü hafif bir Linux alıyoruz (Maven'a artık gerek yok)
# ==========================================
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

# 1. aşamada (builder) üretilen o kusursuz .jar dosyasını alıp bu yeni odaya koyuyoruz
COPY --from=builder /app/target/*.jar app.jar

# Portu aç ve çalıştır
EXPOSE 8080
ENTRYPOINT ["java", "--enable-preview", "-jar", "app.jar"]