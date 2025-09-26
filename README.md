# Financial management api

It is an API for controlling personal expenses where I can initially register users and each user can register their income and expenses
and include a category for them and generate reports based on that.

## How to run
First of all you need have docker installed in your machine
Then you can run the command bellow to build a container for your database

```bash
docker run --name financial_management_db \
  -e POSTGRES_DB=financial_management_db \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=123 \
  -p 5432:5432 \
  -d postgres:15
```

After that you can run your project with maven command or by the play in Intellij

## Things TODO
- Add a job for add the recurring expenses or income all month +2 [Ok]
- Add support to define a balance for each category and see the resume
- Statics about pasts months and evolutions