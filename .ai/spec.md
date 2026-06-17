# Spec

## Outcome

Define the first large-mode execution baseline for implementing PRD V1:

- role-based portal home
- academic core queries for students
- teaching view for teachers
- baseline dashboard for leaders

The output of this task is a set of large-mode task artifacts and human-readable planning docs that are specific enough to start implementation in the next coding task.

## Constraints

- keep the current `RuoYi-Vue` family scaffold and current frontend stack
- preserve `ruoyi-framework`, `ruoyi-common`, and `ruoyi-system` unless blocked
- prefer compatible GitHub references over greenfield design
- keep new business code under `com.ruoyi.campus.*`
- use `campus_`-prefixed SQL tables
- keep V1 focused on read-first, portal-first user value

## Non-Goals

- no V2/V3 implementation in this task
- no business code implementation in this task
- no framework migration to Vue 3, RuoYi-Cloud, or another scaffold
- no direct wholesale import of third-party campus, OA, or pay systems
- no detailed final database schema contract yet
