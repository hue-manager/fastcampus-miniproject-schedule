package com.fastcampus.schedule.schedules.repository;

import com.fastcampus.schedule.schedules.Schedule;
import com.fastcampus.schedule.schedules.constant.Category;
import com.fastcampus.schedule.user.constant.Role;
import com.fastcampus.schedule.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class ScheduleRepositoryTest {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void testFindSchedulesByUserAndPeriod() {


        Long userId = 1L;
        User user = User.of("asdf@gamil.com", "lee", "asdf1234", "010-1111-2222", Role.ROLE_USER);


        Schedule testSchedule = createTestSchedule(user,
                LocalDate.of(2023,3,20),
                LocalDate.of(2023,3,30));
        Schedule testSchedule2 = createTestSchedule(user,
                LocalDate.of(2023,4,20),
                LocalDate.of(2023,4,30));

        List<Schedule> schedules = scheduleRepository.findSchedulesByUserAndPeriod(userId, LocalDate.of(2023,3,20), LocalDate.of(2023,3,30));

        assertThat(schedules).isNotEmpty();
        assertThat(schedules.size()).isEqualTo(1);


    }

    private Schedule createTestSchedule(User user, LocalDate startDate, LocalDate endDate) {

        Schedule schedule = Schedule.of(user, Category.VACATION, startDate, endDate, "test" );

        entityManager.persist(schedule);
        entityManager.flush();

        return schedule;
    }


}