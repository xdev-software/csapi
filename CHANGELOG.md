## 6.0.0
* Published this library as open source
* Ensured build compatibility with Java 8, 11 and 17

*Feel free to contact us or open an issue if you have any questions.*

### Complete overview about the new XDEV (IDE) Framework in version 6
![XDEV-IDE-Framework-6 overview](https://user-images.githubusercontent.com/45384811/134640194-0b42a238-3c7e-402a-8b05-51419108dbbd.png)

### Dropped components
These components are dropped from the XDEV Component Suite.

| Component | Comment |
| -- | -- |
| XdevCalendar | XDEV Calendar is based on the ``MigCalendar`` component which is no longer supported.<br/> https://www.componentsource.com/brand/mig-infocom | 
| XdevTextEditor| Various problems - Does not support Java9+ |
| Synthetica Look And Feels | The Synthetica Look and Feels are no longer part of the XDEV Component Suite, because the integration code did not add that much value to the usage of the Look and Feels.<br/>You can get an up-to-date version of the Look and Feels at https://www.jyloo.com |

*Feel free to contact us if you require integrations for the dropped components*

### New components
All components from the ``biapi-commercial`` got integrated into the ``csapi``.

These include:
* XdevGanttChart
* XdevAreaChart
* XdevBarChart
* XdevLineChart
* XdevPieChart
* XdevPointChart


### Dependencies
You must provide a [repositroy](https://maven.apache.org/guides/mini/guide-multiple-repositories.html) in your `pom.xml` with the JIDE dependencies to run a build.<br/>
These artifacts are available from https://www.jidesoft.com.
