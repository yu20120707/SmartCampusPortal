# Copilot Instructions

Read `AGENTS.md` before making changes.

Classify each non-trivial task as small, medium, or large:

- small: main agent handles directly with local verification
- medium: use a short plan and consider scanner or reviewer roles
- large: use large-mode `.ai/*` artifacts and review gates

This is a **Java / Spring Boot / Vue** project on **Windows**.
Build commands: `mvn clean install` (backend), `npm run dev` (frontend).
Test commands: `mvn test` (backend), `npm run test:unit` (frontend).

Do not store project facts here.
Project facts belong in `docs/ai/*`.
Current task state belongs in `.ai/*`.

If a relevant skill is available, use it as guidance, but do not bypass `AGENTS.md`, review gates, or explicit user constraints.
