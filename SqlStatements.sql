/*********************************************************************************************************************************************/
/*********************************************************************************************************************************************/
/* problem 1 */
/* List a company’s workers by names. */
SELECT name
FROM works_job NATURAL JOIN pays NATURAL JOIN company NATURAL JOIN person
WHERE comp_id = 'ca12';

/*********************************************************************************************************************************************/
/*********************************************************************************************************************************************/
/* problem 2 */
/* List a company’s staff by salary in descending order. */
SELECT name, pay_rate
FROM works_job NATURAL JOIN pays NATURAL JOIN company NATURAL JOIN person
WHERE comp_id = 'aa00'
ORDER BY pay_rate DESC;

/*********************************************************************************************************************************************/
/*********************************************************************************************************************************************/
/* problem 3 */
/* ist companies’ labor cost (total salaries and wage rates by 1920 hours) in descending order. */
WITH salary AS(
  SELECT comp_id, SUM(pay_rate) AS payed
  FROM pays NATURAL JOIN job
  WHERE pay_type = 'salary'
  GROUP BY comp_id
),
wageWorker AS(
  SELECT comp_id, SUM(pay_rate * 1920) AS payed
  FROM pays NATURAL JOIN job
  WHERE pay_type = 'wage'
  GROUP BY comp_id
),
totalCost AS(
  SELECT * 
  FROM salary
  UNION
  SELECT *
  FROM wageWorker
)
SELECT comp_id, SUM(payed) AS amount_payed
FROM totalCost
GROUP BY comp_id
ORDER BY amount_payed DESC;


/*********************************************************************************************************************************************/
/*********************************************************************************************************************************************/
/* problem 4 */
/* Find all the jobs a person is currently holding and worked in the past. */
(SELECT job_title 
FROM person NATURAL JOIN works_job NATURAL JOIN job NATURAL JOIN Job_Profile 
WHERE name = 'Doe' )
    UNION 
(SELECT job_title 
FROM person NATURAL JOIN has_worked NATURAL JOIN job NATURAL JOIN Job_Profile
WHERE name = 'Doe' );

/*********************************************************************************************************************************************/
/*********************************************************************************************************************************************/
/* problem 5 */
/* List a person’s knowledge/skills in a readable format. */
SELECT name, skill_title
FROM person NATURAL JOIN has_skill NATURAL JOIN knowledge_skill
WHERE name = 'Doe';

/*********************************************************************************************************************************************/
/*********************************************************************************************************************************************/
/* problem 6 */
/* List the skill gap of a worker between his/her job(s) and his/her skills. */
SELECT name, ks_code
FROM (  
  /*skills required for worker's current job */
  (SELECT name, ks_code
  FROM person NATURAL JOIN works_job NATURAL JOIN job NATURAL JOIN requires
  WHERE per_id = '0001')
  MINUS
  /* skills person actually has*/
  (SELECT name, ks_code
  FROM person NATURAL JOIN has_skill
  WHERE per_id = '0001')
  );
/*********************************************************************************************************************************************/
/*********************************************************************************************************************************************/
/* problem 7 */
/* List the required knowledge/skills of a job profile in a readable format. */
SELECT skill_title
FROM requires NATURAL JOIN knowledge_skill
WHERE jp_code = '1234';

/*********************************************************************************************************************************************/
/*********************************************************************************************************************************************/
/* problem 8 */
/* List a person’s missing knowledge/skills for a specific job in a readable format. */
SELECT skill_title, ks_code
FROM (( ( SELECT ks_code
        FROM job_profile NATURAL JOIN requires
        WHERE jp_code = '1234')
    MINUS 
      ( SELECT ks_code
        FROM person NATURAL JOIN has_skill
        WHERE name = 'Doe')
    ) NATURAL JOIN knowledge_skill );

/*********************************************************************************************************************************************/
/*********************************************************************************************************************************************/
/* problem 9  */
/* List the courses (course id and title) that each alone teaches all the missing knowledge/skills for a person to pursue a specific job. */
SELECT DISTINCT c_code, course_title
FROM course C
WHERE NOT EXISTS ((	SELECT ks_code
  					FROM job_profile NATURAL JOIN requires
  					WHERE jp_code = '1234')
 			MINUS 
  				(	SELECT ks_code
  					FROM person NATURAL JOIN has_skill
  					WHERE name = 'Doe')
  			MINUS
  				(	SELECT ks_code
  					FROM covers S
  					WHERE C.c_code = S.c_code)
  				);

/*********************************************************************************************************************************************/
/*********************************************************************************************************************************************/
/* problem 10 */
/* Suppose the skill gap of a worker and the requirement of a desired job can be covered by one course. Find the “quickest” solution for this worker. Show the course, section information and the completion date. */

SELECT DISTINCT course_title, sec_no, complete_date
FROM course NATURAL JOIN section NATURAL JOIN covers
WHERE NOT EXISTS (( SELECT ks_code
            FROM job_profile NATURAL JOIN requires
            WHERE jp_code = '1234')
      MINUS 
          ( SELECT ks_code
            FROM person NATURAL JOIN has_skill
            WHERE name = 'Smith')
        MINUS
          ( SELECT ks_code
            FROM covers S
            WHERE c_code = S.c_code)
          )
    AND ROWNUM <= 1
ORDER BY complete_date ASC;

/*********************************************************************************************************************************************/
/*********************************************************************************************************************************************/
/* problem 11 */
/* Find the cheapest course to make up one’s skill gap by showing the course to take and the cost (of the section price). */
SELECT course_title, MIN (retail_price)
FROM course C
WHERE NOT EXISTS ((	SELECT ks_code
  					FROM job_profile NATURAL JOIN requires
  					WHERE jp_code = '1234')
 			MINUS 
  				(	SELECT ks_code
  					FROM person NATURAL JOIN has_skill
  					WHERE name = 'Doe')
  			MINUS
  				(	SELECT ks_code
  					FROM covers S
  					WHERE C.c_code = S.c_code)
  				);

/*********************************************************************************************************************************************/
/*********************************************************************************************************************************************/
/* problem 12 */
/* If query #9 returns nothing, then find the course sets that their combination covers all the missing knowledge/ skills for a person to pursue a specific job. The considered course sets will not include more than three courses. If multiple course sets are found, list the course sets (with their course IDs) in the order of the ascending order of the course sets’ total costs. */

CREATE OR REPLACE TABLE courseSet(
    csetID      number(8,0),
    c_code1     number(6,0),
    c_code2     number(6,0),
    c_code3     number(6,0),
    sizeCS      number(2,0),
    primary key(csetID)
    );
INSERT INTO CourseSet
        SELECT CourseSet_seq.NEXTVAL, C1.c_code, C2.c_code, null, 2 
        FROM Course C1, Course C2
        WHERE C1.c_code < C2.c_code;

INSERT INTO CourseSet
    SELECT CourseSet_seq.NEXTVAL, C1.c_code, C2.c_code, C3.c_code, 3 
    FROM Course C1, Course C2, Course C3
    WHERE C1.c_code < C2.c_code AND C2.c_code < C3.c_code;

WITH missSk AS(
    SELECT ks_code 
    FROM requires
    WHERE jp_code = '1234'
    MINUS
    SELECT ks_code
    FROM has_skill
    WHERE per_id = '0001'
),
CourseSet_Skill(csetID, ks_code) AS (
    SELECT csetID, ks_code 
    FROM CourseSet CSet JOIN covers CS ON CSet.c_code1=CS.c_code 
    UNION
    SELECT csetID, ks_code 
    FROM CourseSet CSet JOIN covers CS ON CSet.c_code2=CS.c_code 
    UNION
    SELECT csetID, ks_code
    FROM CourseSet CSet JOIN covers CS ON CSet.c_code3=CS.c_code
),
/* use division to find those course sets that cover missing skills */ 
Cover_CSet(csetID, sizeCS) AS (
    SELECT csetID, sizeCS
    FROM CourseSet CSet 
    WHERE NOT EXISTS (
        SELECT ks_code
        FROM missSk
        MINUS
        SELECT ks_code
        FROM CourseSet_Skill CSSk WHERE CSSk.csetID = Cset.csetID)
 )
/* to find the smallest sets */
SELECT c_code1, c_code2, c_code3
FROM Cover_CSet NATURAL JOIN CourseSet 
WHERE sizeCS = (SELECT MIN(sizeCS) FROM Cover_CSet)
;



/*********************************************************************************************************************************************/
/*********************************************************************************************************************************************/
/* problem 13 */
/* 13. List all the job profiles that a person is qualified for. */

SELECT J.job_title
FROM job_profile J
WHERE NOT EXISTS(
  (SELECT ks_code
  FROM requires R
  WHERE J.jp_code = R.jp_code
  )
  MINUS
  (SELECT ks_code
  FROM has_skill
  WHERE per_id = '0002'
  )
);

/*********************************************************************************************************************************************/
/*********************************************************************************************************************************************/
/* problem 14*/
/* Find the job with the highest pay rate for a person according to his/her skill qualification. */
SELECT *
FROM (
        SELECT J.job_title, J.avg_pay
        FROM job_profile J
        WHERE NOT EXISTS(
          (SELECT ks_code
          FROM requires NATURAL JOIN job_profile
          WHERE J.jp_code = jp_code
          )
          MINUS
          (SELECT ks_code
          FROM has_skill
          WHERE per_id = '0002'
          )
        )
        ORDER BY J.avg_pay DESC
      )
WHERE ROWNUM <=1;

/*********************************************************************************************************************************************/
/*********************************************************************************************************************************************/
/* problem 15 */
/* List all the names along with the emails of the persons who are qualified for a job profile. */

SELECT P.name, P.email
FROM person P
WHERE NOT EXISTS(
  (SELECT ks_code
  FROM requires NATURAL JOIN job_profile
  WHERE jp_code = '1111'
  )
  MINUS
  (SELECT ks_code
  FROM has_skill NATURAL JOIN person
  WHERE per_id = P.per_id
  )
);

/*********************************************************************************************************************************************/
/*********************************************************************************************************************************************/
/* problem 16 */
/* When a company cannot find any qualified person for a job, a secondary solution is to find a person who is almost qualified to the job. Make a “missing-one” list that lists people who miss only one skill for a specified job profile. */

SELECT P.name
FROM person P
WHERE 1 = (
  SELECT COUNT (ks_code) 
  FROM((SELECT ks_code
    FROM requires NATURAL JOIN job_profile
    WHERE jp_code = '1111'
    )
    MINUS
    (SELECT ks_code
    FROM has_skill NATURAL JOIN person
    WHERE per_id = P.per_id
    ))
);

/*********************************************************************************************************************************************/
/*********************************************************************************************************************************************/
/* problem 17 */
/* List the skillID and the number of people in the missing-one list for a given job profile in the ascending order of the people counts. */

WITH perReqSkillCnt(per_id, skillCnt) AS(
    SELECT per_id, COUNT(ks_code)
    FROM has_skill NATURAL JOIN requires
    WHERE jp_code = '2145'
    GROUP BY per_id),
/* calculates the # of required skills*/
reqSkillCnt(cnt) AS(
    SELECT COUNT(*)
    FROM requires
    WHERE jp_code = '2145'),
/*returns the per_id of those needing only 1 additional skill */
missingOneSk(per_id) AS(
  SELECT per_id
  FROM perReqSkillCnt, reqSkillCnt
  WHERE skillCnt = cnt - 1)
SELECT ks_code, COUNT(*) AS people
FROM knowledge_skill, missingOneSk M
WHERE ks_code = (
                SELECT ks_code
                FROM requires
                WHERE jp_code = '2145'
                MINUS
                SELECT H.ks_code
                FROM has_skill H
                WHERE H.per_id = M.per_id
                )
GROUP BY ks_code
ORDER BY people ASC;



/*********************************************************************************************************************************************/
/*********************************************************************************************************************************************/
/* problem 18 */
/* Suppose there is a new job profile that has nobody qualified. List the persons who miss the least number of skills and report the “least number”. */

/* calculates # of required skills per_id has */
WITH perReqSkillCnt(per_id, skillCnt) AS(
  SELECT per_id, COUNT(ks_code)
  FROM has_skill NATURAL JOIN requires
  WHERE jp_code = '1234'
  GROUP BY per_id),
/* calculates the # of required skills*/
reqSkillCnt(cnt) AS(
  SELECT COUNT(*)
  FROM requires
  WHERE jp_code = '1234')
SELECT MAX(skillCnt), per_id
FROM perReqSkillCnt, reqSkillCnt
WHERE skillCnt = (
  SELECT MAX(skillCnt)
  FROM perReqSkillCnt, reqSkillCnt)
GROUP BY per_id;

/*********************************************************************************************************************************************/
/*********************************************************************************************************************************************/
/* problem 19 */
/* For a specified job profile and a given small number k, make a “missing-k” list that lists the people’s IDs and the number of missing skills for the people who miss only up to k skills in the ascending order of missing skills. */

/* calculates # of required skills per_id has */
WITH perReqSkillCnt(per_id, skillCnt) AS(
  SELECT per_id, COUNT(ks_code)
  FROM has_skill NATURAL JOIN requires
  WHERE jp_code = '1234'
  GROUP BY per_id),
/* calculates the # of required skills*/
reqSkillCnt(cnt) AS(
  SELECT COUNT(*)
  FROM requires
  WHERE jp_code = '1234')
SELECT cnt - skillCnt Skills_Missing, per_id
FROM perReqSkillCnt, reqSkillCnt
WHERE skillCnt > cnt - 3
ORDER BY skillCnt ASC;

/*********************************************************************************************************************************************/
/*********************************************************************************************************************************************/
/* problem 20 */
/*GRAD STUDENT ONLY*/

/*********************************************************************************************************************************************/
/*********************************************************************************************************************************************/
/* problem 21 */
/* ending order of the people counts. (required for graduate students only)
21. In a local or national crisis, we need to find all the people who once held a job of the special job-profile identifier. */

SELECT per_id, name
FROM person NATURAL JOIN has_worked NATURAL JOIN job
WHERE jp_code = '1234';

/*********************************************************************************************************************************************/
/*********************************************************************************************************************************************/
/* problem 22 */
/* Find all the unemployed people who once held a job of the given job-profile identifier. */

((SELECT per_id
FROM person )
MINUS
(SELECT per_id
FROM works_job))
  INTERSECT
(SELECT per_id
FROM has_worked NATURAL JOIN job
WHERE jp_code = '1111');

/*********************************************************************************************************************************************/
/*********************************************************************************************************************************************/
/* probleim 23 */
/* Find out the biggest employer in terms of number of employees or the total amount of salaries and wages paid to employees. */

WITH companyEmployeeCount (comp_id, empCount) AS(
  SELECT comp_id, COUNT(comp_id)
  FROM pays
  GROUP BY comp_id)
SELECT MAX(empCount), comp_id
FROM companyEmployeeCount
GROUP BY comp_id;

/*********************************************************************************************************************************************/
/*********************************************************************************************************************************************/
/* problem 24 */
/* Find out the job distribution among business sectors; find out the biggest sector in terms of number of employees or the total amount of salaries and wages paid to employees. */

SELECT *
FROM (
    WITH company_job_totals (company_name, empCount) AS (
      SELECT company, COUNT(job_code)
      FROM job j
      GROUP BY company
    )
    SELECT primary_sector, empCount
    FROM company NATURAL JOIN company_job_totals
    ORDER BY empCount DESC)
WHERE ROWNUM <=1;


/*********************************************************************************************************************************************/
/*********************************************************************************************************************************************/
/* problem 25 */
/* Find out the ratio between the people whose earnings increase and those whose earning decrease; find the average rate of earning improvement for the workers in a specific business sector. */

SELECT pos/neg
FROM
    (SELECT COUNT(*) AS pos
    FROM job
    WHERE salary_change > 0),
    (SELECT COUNT(*) AS neg
    FROM job
    WHERE salary_change < 0);


WITH daysWorked AS(
    SELECT sysdate - date_started diff, pay_type, job_code, salary_change, pay_rate
    FROM job NATURAL JOIN pays NATURAL JOIN company
    WHERE primary_sector = 'Technology'
),
avgChangePersonSalary AS(
    SELECT salary_change/diff AS finalDiff, job_code
    FROM daysWorked
    WHERE pay_type = 'salary'
),
wageToSalary AS(
    SELECT diff * 5 * pay_rate currentMaking, diff * 5 * (pay_rate - salary_change) startedMaking, job_code, diff
    FROM daysWorked
    WHERE pay_type = 'wage'
),
wageDifference AS(
    SELECT currentMaking - startedMaking wageDiff, diff, job_code
    FROM wageToSalary
),
avgChangePersonWage AS (
    SELECT wageDiff/diff as finalDiff, job_code
    FROM wageDifference
),
combineChangePerson AS (
    SELECT *
    FROM avgChangePersonWage
    UNION
    SELECT *
    FROM avgChangePersonSalary
)
SELECT SUM(finalDiff) / COUNT(*)
FROM combineChangePerson;


/*********************************************************************************************************************************************/
/*********************************************************************************************************************************************/
/* problem 26 */
/* Find the job profiles that have the most openings due to lack of qualified workers. If there are many opening jobs of a job profile but at the same time there are many qualified jobless people. Then training cannot help fill up this type of job. What we want to find is such a job profile that has the largest difference between vacancies (the unfilled jobs of this job profile) and the number of jobless people who are qualified for this job profile. */

WITH joblessPer(per_id) AS(
    SELECT per_id
    FROM person
    MINUS
    SELECT per_id
    FROM works_job
),
openJobs AS(
    SELECT job_code
    FROM job
    MINUS
    SELECT job_code
    FROM works_job
),
openJobsAdd AS(
  SELECT job_code, jp_code
  FROM openJobs JOIN job USING (job_code)
),
qualified(per_id, jp_code) AS(
  SELECT DISTINCT per_id, jp_code
  FROM openJobsAdd O, joblessPer J
  WHERE NOT EXISTS(
    SELECT ks_code
    FROM requires NATURAL JOIN openJobsAdd
    WHERE job_code = O.job_code
    MINUS
    SELECT ks_code
    FROM has_skill NATURAL JOIN joblessPer
    where per_id = J.per_id
    )
),
numOpeningsByPos AS(
  SELECT COUNT(job_code) AS numOpenPos, jp_code
  FROM openJobsAdd
  GROUP BY jp_code
),
numQualifiedPeople AS(
  SELECT COUNT(Distinct per_id) as numQualPeople, jp_code
  FROM qualified NATURAL JOIN job
  GROUP BY jp_code
)
SELECT A.jp_code, A.numOpenPos - B.numQualPeople
FROM numOpeningsByPos A, numQualifiedPeople B
WHERE A.jp_code = B.jp_code;





/*********************************************************************************************************************************************/
/*********************************************************************************************************************************************/
/* problem 27 */
/* Find the courses that can help most jobless people find a job by training them toward the job profiles that have the most openings due to lack of qualified workers. */

WITH joblessPer(per_id) AS(
    SELECT per_id
    FROM person
    MINUS
    SELECT per_id
    FROM works_job
),
openJobs AS(
    SELECT job_code
    FROM job
    MINUS
    SELECT job_code
    FROM works_job
),
openJobsAdd AS(
  SELECT job_code, jp_code
  FROM openJobs JOIN job USING (job_code)
),
qualified(per_id, jp_code) AS(
  SELECT DISTINCT per_id, jp_code
  FROM openJobsAdd O, joblessPer J
  WHERE NOT EXISTS(
    SELECT ks_code
    FROM requires NATURAL JOIN openJobsAdd
    WHERE job_code = O.job_code
    MINUS
    SELECT ks_code
    FROM has_skill NATURAL JOIN joblessPer
    where per_id = J.per_id
    )
),
numOpeningsByPos AS(
  SELECT COUNT(job_code) AS numOpenPos, jp_code
  FROM openJobsAdd
  GROUP BY jp_code
),
numQualifiedPeople AS(
  SELECT COUNT(Distinct per_id) as numQualPeople, jp_code
  FROM qualified NATURAL JOIN job
  GROUP BY jp_code
),
maxJobPos (value) AS (
  SELECT MAX(A.numOpenPos - B.numQualPeople)
  FROM numOpeningsByPos A, numQualifiedPeople B
  WHERE A.jp_code = B.jp_code
),
availPos (jp_code, difference ) AS(
  SELECT A.jp_code , A.numOpenPos - B.numQualPeople
  FROM numOpeningsByPos A, numQualifiedPeople B
  WHERE A.jp_code = B.jp_code
),
maxPos AS(
  SELECT jp_code
  FROM availPos, maxJobPos
  WHERE availPos.difference = maxJobPos.value
),
skRequired AS(
  SELECT ks_code
  FROM maxPos NATURAL JOIN requires
)
SELECT c_code
FROM skRequired NATURAL JOIN covers;

/*********************************************************************************************************************************************/
/*********************************************************************************************************************************************/
/* problem 28 */
/*GRAD STUDENT ONLY*/


























