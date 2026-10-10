### Database Schema

MySQL database `bpms`. Tables are generated from the JPA entities (`spring.jpa.hibernate.ddl-auto=update`), so the entity classes in `entity/` are the source of truth.

### Overview

A process definition has ordered steps, and each step is assigned to a role. 
When a user starts a process, a process instance is created. 
Work on an instance is tracked as tasks, one per step. 
Users act on tasks (approve, reject, etc.), and those actions are saved. 
Notifications and audit logs are kept on the side.



### roles


| Column | Type | Notes |
|---|---|---|
| id | BIGINT | PK, auto increment |
| name | VARCHAR(50) | not null, unique |

Planned roles: ADMIN, MANAGER, HR, EMPLOYEE, FINANCE.

### users

| Column | Type | Notes |
|---|---|---|
| id | BIGINT | PK, auto increment |
| name | VARCHAR(100) | not null |
| email | VARCHAR(150) | not null, unique |
| password | VARCHAR(255) | not null, stored as a BCrypt hash |
| role_id | BIGINT | not null, FK → roles.id |
| created_at | TIMESTAMP | set on insert |
| updated_at | TIMESTAMP | set on update |
| is_deleted | BOOLEAN | not null, default false (soft delete) |

### process_definitions
| Column | Type | Notes |
|---|---|---|
| id | BIGINT | PK, auto increment |
| name | VARCHAR(150) | not null |
| description | VARCHAR(500) | not null |
| version | INT | not null |
| created_by | BIGINT | FK → users.id |

### process_step
| Column | Type | Notes |
|---|---|---|
| id | BIGINT | PK, auto increment |
| process_definition_id | BIGINT | not null, FK → process_definitions.id |
| assigned_role_id | BIGINT | not null, FK → roles.id |
| step_name | VARCHAR(100) | not null |
| step_order | INT | not null, order of the step in the process |

### process_instances
| Column | Type | Notes |
|---|---|---|
| id | BIGINT | PK, auto increment |
| process_definition_id | BIGINT | not null, FK → process_definitions.id |
| started_by | BIGINT | not null, FK → users.id |
| request_data | JSON | data submitted with the request |
| status | VARCHAR(50) | not null, set to `RUNNING` when created |
| created_at | TIMESTAMP | set on insert |
| updated_at | TIMESTAMP | set on update |

### task
| Column | Type | Notes |
|---|---|---|
| id | BIGINT | PK, auto increment |
| process_instance_id | BIGINT | not null, FK → process_instances.id |
| process_step_id | BIGINT | not null, FK → process_step.id |
| assigned_to_user_id | BIGINT | FK → users.id, can be null (unassigned) |
| status | VARCHAR(30) | not null |
| created_at | TIMESTAMP | set on insert |
| completed_at | TIMESTAMP | null until the task is done |

### task_actions
| Column | Type | Notes |
|---|---|---|
| id | BIGINT | PK, auto increment |
| task_id | BIGINT | not null, FK → task.id |
| action_by | BIGINT | not null, FK → users.id |
| action | VARCHAR(30) | not null, e.g. approve / reject |
| comment | VARCHAR(200) | optional |
| created_at | TIMESTAMP | set on insert |

### notifications
| Column | Type | Notes |
|---|---|---|
| id | BIGINT | PK, auto increment |
| user_id | BIGINT | not null, FK → users.id (who gets it) |
| process_instance_id | BIGINT | not null, FK → process_instances.id |
| task_id | BIGINT | FK → task.id, optional |
| message | VARCHAR(500) | not null |
| is_read | BOOLEAN | not null, default false |
| created_at | TIMESTAMP | set on insert |

### audit_log
| Column | Type | Notes |
|---|---|---|
| id | BIGINT | PK, auto increment |
| user_id | BIGINT | not null, FK → users.id |
| action | VARCHAR(50) | not null |
| entity_type | VARCHAR(50) | not null |
| entity_id | BIGINT | not null |
| details | VARCHAR(500) | optional |
| created_at | TIMESTAMP | set on insert |

## Relationships

| Parent | Child | Foreign key |
|---|---|---|
| roles | users | users.role_id |
| roles | process_step | process_step.assigned_role_id |
| users | process_definitions | process_definitions.created_by |
| users | process_instances | process_instances.started_by |
| users | task | task.assigned_to_user_id |
| users | task_actions | task_actions.action_by |
| users | notifications | notifications.user_id |
| users | audit_log | audit_log.user_id |
| process_definitions | process_step | process_step.process_definition_id |
| process_definitions | process_instances | process_instances.process_definition_id |
| process_instances | task | task.process_instance_id |
| process_instances | notifications | notifications.process_instance_id |
| process_step | task | task.process_step_id |
| task | task_actions | task_actions.task_id |
| task | notifications | notifications.task_id |

## Known issues / to do

- Table names are inconsistent: `process_step`, `task` and `audit_log` are singular, the rest are plural. Rename them to plural.
- `status` and `action` columns are plain strings. Replace them with enums.
- `roles` has no seed data yet, so the roles have to be inserted by hand.
- `process_definitions.created_by` should probably be `NOT NULL`.