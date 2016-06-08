# PL4 - DART-N

## Sprint
| Group                               | Sprint Management |           |                |                       |                |            |
|-------------------------------------|-------------------|-----------|----------------|-----------------------|----------------|------------|
|                                     | user story        |           |                | learning from history |                |            |
|                                     | definition        | splitting | responsibility | estimation            | prioritisation | reflection |
| PL4 (DART-N)                        | 3                 | 10        | 10             | 8                     | 7              | 5          |

## Code Evolution
| Group                               | Code Evolution Quality |         |                     |                  |        |          |                        |         |         |                        |             |
|-------------------------------------|------------------------|---------|---------------------|------------------|--------|----------|------------------------|---------|---------|------------------------|-------------|
|                                     | architecture           |         |                     | code readability |        |          | continuous integration |         |         | pull-based development |             |
|                                     | changes                | arch-dd | code change quality | formatting       | naming | comments | building               | testing | tooling | branching              | code review |
| PL4 (DART-N)                        | 5                      | 4       | 6                   | 5                | 9      | 5        | 9                      | 3       | 3       | 10                     | 5           |

## Notes
Sprint
Definition: 3

- Refactor for what purpose?
- "Deal with inversion in graph" How? Or make a point on 'finding out how' and a point on 'implementing'
- Describe what you are 'fixing'
- At this stage testing should not be a part of the sprint anymore
- Repeating feedback

Splitting: 10

Responsibility: 10

Reflection
Estimation: 8

Prioritisation: 7

- If you start using B.2.4 it is better to use A-E, this is vague

Reflection: 5

- Define 'bug-free'
- No reactions on how problems will be prevented in the future. 
- Elaborate on the extra hours made 

Code Evolution
Architecture
Changes: 5 
Document: 4

- No new version
- Feedback should be implemented

CQ: 6
- Same feedback as last week
- Seen some code that can be optimized (LineageColor is one giant duplication)
- Controllers adding lots of GUI parts

Code Readability
Formatting: 5
Naming: 9
Comments: 5

- Formatting got significantly worse
- New things not commented at all
- At this stage this should be done when programming 

CI
Building: 9
Testing: 3

- By far not enough

Tooling: 3

- Everything has errors

PBD
Branching: 10
Code Review: 5

- Sometimes people merging their own PRs
- Still lots of PRs with no comments
- Things should be fixed, I see sometimes that things are noted "for the future" which are forgotten
