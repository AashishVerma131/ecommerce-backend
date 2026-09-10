# ID migration

All application-owned IDs are now String/VARCHAR IDs with entity prefixes.

Examples:
- users: `usr_01` (existing records are migrated from numeric IDs)
- products: `prod_01`
- carts: `cart_01`
- cart items: `item_01`
- orders: `ord_01`
- order items: `orditem_01`
- payments: `pay_01`

New IDs are generated safely by the application as `<prefix>_<12 hex characters>`.
For example: `usr_a13f09c2d4e1`.

The Flyway migration `V9__convert_ids_to_prefixed_strings.sql` preserves existing rows and relationships.
Do not edit V1-V8 after they have already been applied by Flyway; V9 performs the conversion.
