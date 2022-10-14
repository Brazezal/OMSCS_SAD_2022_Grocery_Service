default: backend

up:
	docker-compose -p gatech -f docker-compose.yml up -d

clean:
	docker-compose -p gatech -f docker-compose.yml down

.PHONY: backend
backend:
	docker build -t gatech/backend -f ./images/Dockerfile.backend ./backend

