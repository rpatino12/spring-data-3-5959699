package com.example.university.dao;

import com.example.university.business.CourseFilter;
import com.example.university.business.DynamicQueryService;
import com.example.university.business.UniversityService;
import com.example.university.domain.Department;
import com.example.university.domain.Staff;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static com.example.university.business.CourseFilter.filterBy;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test Criteria-based queries
 */
@SpringBootTest
public class CriteriaQueryTest {

    @Autowired
    private DynamicQueryService queryService;
    @Autowired
    private UniversityService universityService;
    @Autowired
    private DepartmentDao departmentDao;
    @Autowired
    private StaffDao staffDao;

    @Test
    void findByCriteria() {
        UniversityFactory.fillUniversity(universityService);
        Department humanities = departmentDao.findByName("Humanities").get();
        Staff professorBlack = staffDao.findByLastName("Black")
                .stream().findFirst().get();

        System.out.println('\n' + "*** All Humanities Courses");
        find(filterBy().department(humanities));

        System.out.println('\n' + "*** 4 credit courses");
        find(filterBy().credits(4));

        System.out.println('\n' + "*** Courses taught by Professor Black");
        find(filterBy().instructor(professorBlack));

        System.out.println('\n' + "*** 4 Credit Courses In Humanties, taught by Professor Black");
        find(filterBy().department(humanities).credits(4)
                .instructor(professorBlack));
    }

    private void find(CourseFilter filter) {
        queryService.findCoursesByCriteria(filter)
            .forEach(course -> {
                assertTrue(filter.meetsCriteria(course));
                System.out.println(course);
            });
    }
}