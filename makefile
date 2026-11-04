.PHONY: run build test clean

# Levanta la aplicación
run:
	./mvnw spring-boot:run

# Compila el proyecto saltando pruebas
build:
	./mvnw clean package -DskipTests

# Ejecuta los tests unitarios
test:
	./mvnw test

# Limpia los archivos generados
clean:
	./mvnw clean
