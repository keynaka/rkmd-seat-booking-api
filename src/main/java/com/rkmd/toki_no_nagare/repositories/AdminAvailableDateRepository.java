package com.rkmd.toki_no_nagare.repositories;

import com.rkmd.toki_no_nagare.entities.admin_available_date.AdminAvailableDate;
import com.rkmd.toki_no_nagare.entities.admin_available_date.AdminAvailableDateId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminAvailableDateRepository extends JpaRepository<AdminAvailableDate, AdminAvailableDateId> {
}
