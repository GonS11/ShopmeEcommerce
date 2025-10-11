# 🛒 Shopme E-Commerce

A complete e-commerce system built with Java and Spring Boot, featuring a
comprehensive admin panel and customer-facing shopping site.

## 📋 Description

Shopme is a professional full-stack e-commerce application consisting of two
main applications:

- **ShopmeBackend (Admin)**: Administrative panel for managing the entire system
- **ShopmeFrontend (Shopping)**: Customer-facing online store

## 🚀 Technologies

### Backend

- Java 21
- Spring Boot 3.2.0
- Spring Data JPA
- Spring Security + JWT
- Hibernate ORM
- MySQL 8
- Maven
- Lombok

### Frontend

- Thymeleaf Template Engine
- Bootstrap 4
- jQuery 3
- HTML5/CSS3
- JavaScript

## 📁 Project Structure

```
ShopmeEcommerce/
├── ShopmeCommon/              # Shared entities and common code
│   ├── src/main/java/
│   │   └── com/shopme/common/
│   │       ├── entity/        # JPA entities
│   │       ├── exception/     # Custom exceptions
│   │       └── constants/     # System constants
│   └── pom.xml
├── ShopmeWebParent/           # Parent module for web applications
│   ├── ShopmeBackend/         # Admin Application (Port 8080)
│   │   ├── src/main/java/
│   │   │   └── com/shopme/admin/
│   │   │       ├── controller/
│   │   │       ├── service/
│   │   │       ├── repository/
│   │   │       └── security/
│   │   ├── src/main/resources/
│   │   │   ├── application.properties
│   │   │   ├── static/
│   │   │   └── templates/
│   │   └── pom.xml
│   ├── ShopmeFrontend/        # Shopping Application (Port 8081)
│   │   ├── src/main/java/
│   │   │   └── com/shopme/
│   │   ├── src/main/resources/
│   │   └── pom.xml
│   └── pom.xml
├── pom.xml                    # Parent POM
├── .gitignore
└── README.md
```

## 🗄️ Database Setup

### MySQL Configuration

1. **Create Databases:**

```sql
-- Backend database
CREATE DATABASE shopmedb_backend
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- Frontend database (optional, can share the same)
CREATE DATABASE shopmedb_frontend
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;
```

2. **Create User and Grant Privileges:**

```sql
-- Create user
CREATE USER 'shopmeuser'@'localhost' IDENTIFIED BY 'shopme2024';

-- Grant privileges
GRANT ALL PRIVILEGES ON shopmedb_backend.* TO 'shopmeuser'@'localhost';
GRANT ALL PRIVILEGES ON shopmedb_frontend.* TO 'shopmeuser'@'localhost';

-- Apply changes
FLUSH PRIVILEGES;
```

## ⚙️ Installation & Setup

### Prerequisites

- JDK 17 or higher
- MySQL 8.0 or higher
- Maven 3.6+
- Git

### Step-by-Step Installation

1. **Clone the repository:**

```bash
git clone https://github.com/YOUR_USERNAME/ShopmeEcommerce.git
cd ShopmeEcommerce
```

2. **Configure Database:**

   - Execute the SQL scripts above to create databases and user
   - Update `application.properties` files if needed

3. **Build the project:**

```bash
mvn clean install
```

4. **Run Backend Application:**

```bash
cd ShopmeWebParent/ShopmeBackend
mvn spring-boot:run
```

5. **Run Frontend Application (in another terminal):**

```bash
cd ShopmeWebParent/ShopmeFrontend
mvn spring-boot:run
```

### Application URLs

- **Admin Panel**: http://localhost:8080/ShopmeAdmin
- **Shopping Site**: http://localhost:8081

## 📦 Features

### Admin Module (Backend)

- [x] User Management
  - Create, update, delete users
  - Role-based access control
  - User authentication & authorization
- [ ] Category Management
  - Hierarchical categories
  - Enable/disable categories
- [ ] Brand Management
  - Brand CRUD operations
  - Logo upload
- [ ] Product Management
  - Product catalog
  - Multiple images per product
  - Stock management
  - Pricing & discounts
- [ ] Customer Management
  - Customer information
  - Order history
- [ ] Order Management
  - View and update order status
  - Track shipments
  - Handle customer inquiries
- [ ] Shipping Management
  - Configure shipping methods
  - Set shipping rates
  - Delivery timeframes
- [ ] Sales Reports
  - Revenue analytics
  - Sales by category/product
  - Export reports

### Shopping Module (Frontend)

- [ ] Product Browsing
  - Category navigation
  - Product search
  - Filtering & sorting
- [ ] Product Details
  - Images gallery
  - Description & specifications
  - Customer reviews
- [ ] Shopping Cart
  - Add/remove items
  - Update quantities
  - Calculate totals
- [ ] Checkout Process
  - Shipping information
  - Payment integration
  - Order confirmation
- [ ] Customer Account
  - Registration & login
  - Profile management
  - Order history
- [ ] Order Tracking
  - Track shipment status
  - Estimated delivery date
- [ ] Product Reviews
  - Rate products
  - Write reviews
  - View ratings

## 🔐 Security

- Spring Security with JWT authentication
- Password encryption with BCrypt
- Role-based access control (RBAC)
- CSRF protection
- Secure session management

## 🧪 Testing

### Run all tests:

```bash
mvn test
```

### Run specific test class:

```bash
mvn test -Dtest=UserRepositoryTests
```

### Run specific test method:

```bash
mvn test -Dtest=UserRepositoryTests#testCreateUser
```

## 🛠️ Development

### Project Dependencies

The project uses a multi-module Maven structure:

- **ShopmeCommon**: Shared entities and utilities
- **ShopmeWebParent**: Common web dependencies
  - **ShopmeBackend**: Admin application
  - **ShopmeFrontend**: Shopping application

### Database Schema

The application uses Hibernate with `ddl-auto=update` for development.

Main entities:

- User (users table)
- Role (roles table)
- Category (categories table)
- Brand (brands table)
- Product (products table)
- Customer (customers table)
- Order (orders table)
- OrderDetail (order_details table)
- And more...

### Code Style

- Follow Java naming conventions
- Use meaningful variable and method names
- Document complex logic with comments
- Keep methods focused and concise

## 📊 Database Diagram

```
users ──────┐
            │ Many-to-Many
            ├─── users_roles
            │
roles ──────┘

categories (hierarchical/self-referencing)

brands ──────┐
             │ Many-to-One
             ├─── products
             │
categories ──┘

products ─────── product_images
products ─────── product_details

customers ──────┐
                │ One-to-Many
                ├─── orders
                │
addresses ──────┘

orders ─────── order_details ─────── products
orders ─────── order_tracks
```

## 🚀 Deployment

### Production Configuration

1. Update `application.properties`:

```properties
# Change to 'validate' or 'none' in production
spring.jpa.hibernate.ddl-auto=validate

# Disable SQL logging
spring.jpa.show-sql=false

# Enable Thymeleaf cache
spring.thymeleaf.cache=true
```

2. Build production JAR:

```bash
mvn clean package -Pprod
```

3. Run the application:

```bash
java -jar ShopmeBackend/target/ShopmeBackend-1.0.jar
java -jar ShopmeFrontend/target/ShopmeFrontend-1.0.jar
```

## 📝 API Documentation

API endpoints are documented using Swagger/OpenAPI.

Access Swagger UI at: http://localhost:8080/swagger-ui.html

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Commit Message Convention

```
feat(scope): add new feature
fix(scope): fix bug
docs(scope): update documentation
style(scope): format code
refactor(scope): refactor code
test(scope): add tests
chore(scope): maintenance tasks
```

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file
for details.

## 👥 Authors

- **Your Name** - _Initial work_ -
  [YourGitHub](https://github.com/YOUR_USERNAME)

## 🙏 Acknowledgments

- Inspired by real-world e-commerce platforms
- Built with Spring Boot best practices
- UI components from Bootstrap
- Icons from Font Awesome

## 📧 Contact

For questions or suggestions, please open an issue on GitHub.

## 📚 Documentation

For detailed documentation, visit the
[Wiki](https://github.com/YOUR_USERNAME/ShopmeEcommerce/wiki)

## 🔗 Useful Links

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Thymeleaf Documentation](https://www.thymeleaf.org/documentation.html)
- [Bootstrap Documentation](https://getbootstrap.com/docs/4.6/getting-started/introduction/)
- [MySQL Documentation](https://dev.mysql.com/doc/)

---

⭐ If you find this project useful, please consider giving it a star on GitHub!

## 🐛 Known Issues

No known issues at this time. Please report any bugs you find.

## 📈 Roadmap

- [ ] Implement payment gateway integration
- [ ] Add email notifications
- [ ] Implement product recommendations
- [ ] Add social media login
- [ ] Mobile app development
- [ ] REST API for third-party integrations
- [ ] Advanced analytics dashboard
- [ ] Multi-language support
- [ ] Multi-currency support
- [ ] Inventory management system

## 💻 System Requirements

### Minimum Requirements

- CPU: 2 cores
- RAM: 4GB
- Storage: 10GB free space
- OS: Windows 10, macOS 10.14+, or Linux

### Recommended Requirements

- CPU: 4+ cores
- RAM: 8GB+
- Storage: 20GB+ free space
- SSD for better performance

## 🔧 Troubleshooting

### Common Issues

**Issue**: Port 8080 already in use **Solution**: Change the port in
`application.properties`:

```properties
server.port=8082
```

**Issue**: MySQL connection refused **Solution**:

- Check if MySQL service is running
- Verify username and password in `application.properties`
- Check MySQL is listening on port 3306

**Issue**: Build fails **Solution**:

```bash
mvn clean install -U
```

## 📞 Support

If you need help, you can:

- Open an issue on GitHub
- Check the documentation
- Contact the development team

## 🎓 Learning Resources

- [Spring Boot Tutorial](https://spring.io/guides)
- [Thymeleaf Tutorial](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html)
- [MySQL Tutorial](https://dev.mysql.com/doc/mysql-tutorial-excerpt/8.0/en/)
- [Maven Guide](https://maven.apache.org/guides/)

---

**Made with ❤️ using Spring Boot**
