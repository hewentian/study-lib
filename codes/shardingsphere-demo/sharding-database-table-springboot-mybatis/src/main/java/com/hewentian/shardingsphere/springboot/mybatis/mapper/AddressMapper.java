package com.hewentian.shardingsphere.springboot.mybatis.mapper;

import com.hewentian.shardingsphere.entity.Address;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AddressMapper {

    void createTableIfNotExists();

    void truncateTable();

    void dropTable();

    void insert(Address address);

    void delete(long addressId);

    List<Address> selectAll();
}
