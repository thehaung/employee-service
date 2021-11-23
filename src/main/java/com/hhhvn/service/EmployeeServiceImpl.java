package com.hhhvn.service;

import com.hhhvn.VO.Department;
import com.hhhvn.VO.ResponseTemplateVO;
import com.hhhvn.enntity.Employee;
import com.hhhvn.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService{
    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    RestTemplate restTemplate;

    @Override
    public Employee saveEmployee(Employee employee){
        log.info("Inside save");
        return employeeRepository.save((employee));
    }
    @Override
    public List<Employee> getAllEmployee() {
        log.info("Inside getAll");
        List<Employee> list= employeeRepository.findAll();
        return list;
    }

    public ResponseTemplateVO getEmployeeWithDepartment(Long id){
        ResponseTemplateVO vo = new ResponseTemplateVO();
        Employee employee = employeeRepository.findById(id).get();
        vo.setEmployee(employee);
        Department department = restTemplate.getForObject("http://localhost:9001/departments/"
                +employee.getDepartmentId(), Department.class);
        vo.setDepartment(department);
        return vo;
    }
    @Override
    public HttpStatus deleteEmployee(Long id) {
        log.info("Inside delete");
        try {
            employeeRepository.deleteById(id);
            return HttpStatus.OK;
        } catch (Exception e) {
            // TODO: handle exception
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "NOT FOUND");
        }

    }


    @Override
    public Employee updateEmployeeId(Long id, Employee employeeData) {
        log.info("Inside update");

        try{
        Employee employee = employeeRepository.findById(id).get();
            employee.setFirstName(employeeData.getFirstName());
            employee.setLastName(employeeData.getLastName());
            employee.setEmail(employeeData.getEmail());
            return employeeRepository.save(employee);
        } catch (Exception e) {
            // TODO: handle exception
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "NOT FOUND");
        }

    }

}

