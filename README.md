# multi-plugin-sample

A minimal end-to-end sample showing how to bundle **multiple Paper plugins**
into a single Grounds push using the `plugins:` field in `grounds.yaml`.

Two trivial sub-projects ship a custom join and quit message; both land on
the same Paper server in one push.

```
multi-plugin-sample/
├── grounds.yaml          # bundles :welcome + :goodbye
├── settings.gradle.kts   # multi-module setup
├── build.gradle.kts      # shared toolchain + Paper dep
├── welcome/              # WelcomePlugin — PlayerJoinEvent → "Welcome, <name>!"
└── goodbye/              # GoodbyePlugin — PlayerQuitEvent → "Bye, <name>!"
```

## Why this exists

Without `plugins:`, every push deploys exactly one plugin onto its own Paper
server. To exercise interaction between plugins (chat ↔ economy, party ↔
matchmaker, etc.) you'd have to build a fat-jar locally — annoying and
non-portable.

`plugins:` lets the manifest list 2–10 sources that the Gradle plugin packs
into a single tar.gz upload. Forge unpacks them into `/app/plugins/` on one
running server, in manifest order.

## The manifest

```yaml
name: multi-plugin-sample
type: plugin-paper
baseImage: paper
plugins:
  - :welcome
  - :goodbye
```

Two `:gradle-project` references. `groundsPush` resolves each to its
`shadowJar` / `jar` task and wires `dependsOn` automatically — you don't
need to know where the JARs land on disk.

Other source kinds are also accepted; mix and match as needed:

```yaml
plugins:
  - :welcome                                          # this repo's :welcome
  - modules/legacy/build/libs/legacy.jar              # local relative path
  - /opt/jars/external.jar                            # local absolute (warns)
  - github:groundsgg/some-plugin@v1.4.2               # GitHub release (groundsgg only)
  - github:groundsgg/some-plugin@v1.4.2:specific.jar  # GH release, named asset
```

GitHub sources are restricted to the `groundsgg/*` org by both the Gradle
plugin and the forge admission gate — see the [manifest reference][docs] for
the full security policy.

## Push it

```bash
./gradlew groundsPush
```

Behind the scenes:

1. `:welcome:jar` and `:goodbye:jar` build (plain Paper plugin JARs)
2. Both JARs land in a tar.gz under `build/grounds-push/bundle.tar.gz`
3. `groundsPush` POSTs the tar.gz + manifest to forge
4. Forge runs kaniko: `FROM ghcr.io/groundsgg/paper\nCOPY plugins/ /app/plugins/`
5. The resulting image is deployed; both plugins boot together

You can connect at `multi-plugin-sample-<your-handle>.mc.grnds.io`. Joining
prints `Welcome, <name>!`; leaving prints `Bye, <name>!`.

## Pre-requisites

- JDK 21
- A Grounds account — `grounds login` writes `credentials.json`, or set
  `GROUNDS_TOKEN` in the environment for CI use
- Membership in any project (the push lands in your default project unless
  you pass `--project`)

## Further reading

- [Manifest reference][docs] — every `grounds.yaml` field, including the
  full schema for `plugins:` source kinds
- [`groundsPush` task reference][gradle-docs] — flags, extension config,
  and CI wiring
- [Pushes concept][pushes] — what happens between `groundsPush` and a
  running pod

[docs]: https://grounds.gg/docs/build/manifest
[gradle-docs]: https://grounds.gg/docs/build/gradle-plugin/groundsPush
[pushes]: https://grounds.gg/docs/build/concepts/pushes
