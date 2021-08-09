[![Latest version](https://img.shields.io/maven-central/v/com.xdev-software/csapi)](https://mvnrepository.com/artifact/com.xdev-software/csapi)
[![Build](https://img.shields.io/github/workflow/status/xdev-software/csapi/Check%20Build/develop)](https://github.com/xdev-software/csapi/actions/workflows/checkBuild.yml?query=branch%3Adevelop)
[![javadoc](https://javadoc.io/badge2/com.xdev-software/csapi/javadoc.svg)](https://javadoc.io/doc/com.xdev-software/csapi) 

# XDEV Component Suite (csapi)

Java Swing provides a significant set of controls for developing graphical user interfaces (GUI), for example, buttons, labels, tabs, all formular-controls, table, tree and techniques about windowing / scenes. Many standard applications can easily be created with that. If you need innovative business applications and want to delight your users, you obviously need more.

The XDEV Component Suite provides you ultimate power features, which extremely upgrades your application and makes it multiple times more capable and comfortable. Extend your solutions with features your users only dream about yet. Best of all - the integrations is amazingly easy!

Before version 6.0.0, csapi was distributed as commercial software. We provide this framework that was built on top of JIDE Components as open-source. However, to use it you have to license the base components from JIDE [https://www.jidesoft.com](https://www.jidesoft.com). Mention that you are using XDEV Component Suite and get a discounted license.

## Migration Guide to version 6+
### Dropped Components in version 6.0.0
These components are dropped from the XDEV Component Suite.

| Component | Comment |
| -- | -- |
| XDEVCalendar | XDEV Calendar is based on the MigCalendar component which is no longer supported. https://www.componentsource.com/brand/mig-infocom | 
| XdevTextEditor| Does not support Java9+ |
| Synthetica Look And Feels | The Synthetica Look And Feels are no longer part of the XDEV Component Suite, because the integration code did not add that much value to the usage of the Look and Feels. You can get an up-to-date version of the Look and Feels at https://www.jyloo.com |

### Dependencies
You must provide a [repositroy](https://maven.apache.org/guides/mini/guide-multiple-repositories.html) in your `pom.xml` with the JIDE dependencies to run a build.
These artifacts are available from [https://www.jidesoft.com](https://www.jidesoft.com)


## XDEV-IDE
XDEV(-IDE) is a visual Java development environment for fast and easy application development (RAD - Rapid Application Development). XDEV differs from other Java IDEs such as Eclipse or NetBeans, focusing on programming through a far-reaching RAD concept. The IDE's main components are a Swing GUI builder, the XDEV Application Framework, and numerous drag-and-drop tools and wizards with which the functions of the framework can be integrated.

The XDEV-IDE was license-free up to version 4 inclusive and is available for Windows, Linux and macOS. From version 5, the previously proprietary licensed additional modules are included in the IDE and the license of the entire product has been converted to a paid subscription model. The XDEV Application Framework, which represents the core of the RAD concept of XDEV and is part of every XDEV application, was released as open-source in 2008.

## Contributing

We would absolutely love to get the community involved, and we welcome any form of contributions – comments and questions on different communication channels, issues and pull request in the repositories, and anything that you build and share using our components.

### Get in touch with the team

Twitter: https://twitter.com/xdevsoftware<br/>
Mail: opensource@xdev-software.de

### Some ways to help:

- **Report bugs**: File issues on GitHub.
- **Send pull requests**: If you want to contribute code, check out the development instructions below.

We encourage you to read the [contribution instructions by GitHub](https://guides.github.com/activities/contributing-to-open-source/#contributing) also.

## Dependencies and Licenses
The XDEV csapi is released under [GNU Lesser General Public License version 3](https://www.gnu.org/licenses/lgpl-3.0.en.html) aka LGPL 3
View the [summary of all dependencies online](https://xdev-software.github.io/csapi/dependencies/)

## Releasing [![Build](https://img.shields.io/github/workflow/status/xdev-software/csapi/Release?label=Release)](https://github.com/xdev-software/csapi/actions/workflows/release.yml)
Consider doing a [test-deployment](https://github.com/xdev-software/csapi/actions/workflows/test-deploy.yml?query=branch%3Adevelop) before actually releasing.

If the ``develop`` is ready for release, create a pull request to the ``master``-Branch and merge the changes.

When the release is finished do the following:
* Merge the auto-generated PR (with the incremented version number) back into the ``develop``
* Add the release notes to the [GitHub release](https://github.com/xdev-software/csapi/releases/latest)
