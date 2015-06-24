Dependencies and configuration for pivot_contrib MDA

This project provides all dependencies and configuration for minimalistic MDA (model driven architecture)
generation of source codes of GUI implementaion based on Apache Pivot framework.

The MDA makes use of following components:
- Model - is defined by POJO classes annotated by pivot_contrib.mda annotations. Theese anotations define MDA pattern
	to be used to generate anotated POJO model class by @Streotype(patern="...") anotation.
- MDA generator - Dirigent mda generator configured to use CLASSLOADER metafacade factory is used to generate outputs
- MDA patterns - defined in pivot_contrib.patterns package. Each pattern is defined in single xml file. 
	Each pattern may consist from one or more steps. Each step defines one output file generated
	using Velocity template.
	
Provided templates / model anotations:
@ValueObject - generates value object generator. This generator is used to generate given value object to be used
	by implmentation of unit test and mock services.
@Form	- generates form and  validator files including unit test. The POJO anotated by @Form anotation must extend value object.
	For each value object field will be generated form field and validation rule. The unit test has main method to launch form 
	as indepent Desktop Pivot Application.
@TableView - generates table view files including unit test. The POJO anotated by @Form anotation must extend value object.
	For each value object field will be generated table view column. 
	The unit test has main method to launch table view as indepent Desktop Pivot Application.
@ViewModel - generates model, view, controller files including unit tests. 
	The POJO fields will be generated as model fields. 
	The intent of view bxml file is to include forms, table views and other GUI components.
	The view unit test has main method to launch view as indepent Desktop Pivot Application.

To use pivot_contrib MDA copy Dirigent eclipse plugin (https://code.google.com/p/dirigent/downloads/detail?name=dirigent.eclipse_1.0.0.201309202315.jar)
to your eclipse (v 3.5+) plugin folder. 
Create new Java project and add pivot_contrib.mda-dependencies 
as required project on Eclipse Java Build Path.
To generate model class right click on class in Package Explorer, and select Dirigent -> Generate in pop-up menu.

	
Developer workflow:

	1. Value object
	1) Define value object class anotated by @ValueObject anotation.
	2) Generate outputs using Dirigent eclipse plugin.

	2. Form
	1) Define form class extending value object class anotated by @Form anotation.
	2) Generate outputs using Dirigent eclipse plugin.
	Optionally:
		3) Change Validator unit test and implementation.
		4) Change Form unit test and implementation.
		
	3. Table view
	1) Define table view class extending value object class anotated by @Form anotation.
	2) Generate outputs using Dirigent eclipse plugin.
	Optionally:
		3) Change table view unit test and implementation.
		
	4. View model
	1) Define view model class anotated by @ViewModel anotation. 
		For form use define editedRecord field holding edited value object.
		For tableView define tableData List<value object> field holding value objects to be displayed in table view.
	2) Generate outputs using Dirigent eclipse plugin.
	3) Implement unit test, presentation logic.
	   Customize form / table view components to make use of ViewModel using propety binding.
	   (the proposed snippets are already commented out in generated form and table view BXML).
			

 