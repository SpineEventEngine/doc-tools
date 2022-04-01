# Doc Tools
This repository contains tools for working with Javadoc:

 * [`ExcludeInternal`](javadoc-filter/README.md) — a Doclet for removing Javadocs for types
   annotated as `@Internal`.
 * [Javadoc Style Formatter](javadoc-style/README.md) — improves the style of Javadocs of the code
   generated for Protobuf types.
 * [Dokka Extensions](dokka-extensions/README.md) - Module for custom Dokka plugins. There is
   the `ExcludeInternalPlugin`, which excludes code annotated by `@Internal` from documentation.
