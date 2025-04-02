package com.springboot.dev_spring_boot_demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "authorities")
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // New independent primary key

    @Column(name = "authority")
    private String authority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", referencedColumnName = "username")
    private Admin admin; // Foreign key to Admin.username

    @Column(name = "username", insertable = false, updatable = false)
    private String username; // Read-only field for convenience

    public Authority() {
    }

    public Authority(String authority, Admin admin) {
        this.authority = authority;
        this.admin = admin;
        this.username = admin != null ? admin.getUsername() : null; // Optional: set username for convenience
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
        this.username = admin != null ? admin.getUsername() : null; // Keep username in sync
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Note: No setter for username to prevent accidental updates

    @Override
    public String toString() {
        return "Authority{" +
                "id=" + id +
                ", authority='" + authority + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}