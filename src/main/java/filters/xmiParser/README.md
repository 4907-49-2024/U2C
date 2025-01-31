# Module Purpose
Go from XMI file representation, to UML representations in code.

This README defines the scope of each diagram interpretation (what elements we wish to interpret).

## Assumptions (General)
1. Inputs are valid models in Papyrus.
   - This allows us to rely on Papyrus to validate a bunch of implicit assumptions we may have. 

## Inputs
1. XMI file input (The model to convert to UML elements)
2. XMI Meta Model, config file (defines what can exist in the model)
3. XMI Transformations, config file (defines how to map XMI to meta model elements)

Currently, we have an individual custom metamodel and transformation pair for each diagram type.
The default files from the library (xmiTrans2_0 and metamodel2) attempt to read everything, for every tool.

## State Diagrams
Note: Only track relevant information (abstracting), 
there should be more information in the XMI in reality.

Metamodel: stateMetaModel.xml.
Transformations: xmiStateTrans.xml

### Assumptions
1. State Machine names == Agent names AND Agent Names are unique.
   - We use state machine names as a key to associate elements to their diagram. 
2. Transition Names follow this format: inputStim\[guard(optional)]/outStim
   - This is to simplify implementation, and model tool use. "Proper" field setting is tedious.

### Metadata
| Element         | XMI Type     | Can Contain           | Attributes | 
|-----------------|--------------|-----------------------|------------|
| packagedElement | StateMachine | subvertex, transition | name       |

### State
| Element    | XMI Type    | Can Contain        | Attributes | 
|------------|-------------|--------------------|------------|
| subvertex  | Pseudostate | N/A                | kind (*1)  |  
| subvertex  | State       | doActivity, region | name       |  
| doActivity | Activity    |                    | name       |   

(*1): Kind is the type of pseudo-state. 
The only kind we care about is "initial", but papyrus does not set the kind field for initial states...
**Assume** for now that when it's not set, it's an initial state, it seems papyrus sets the kind field for the others.

### Transition
| Element    | XMI Type   | Can Contain | Attributes     | 
|------------|------------|-------------|----------------|
| transition | Transition | N/A         | source, target |

There does exist a way to compartmentalize the transition name into: 
input (trigger), guard (specification symbol), and output (Activity effect). 
The issue is it is both annoying to model and parse.
Instead, we opt to parse the name String according to a strict naming convention (See assumption #2).

Compared to the comment solution, this is better because the convention is already known and followed by designers.
It also simplifies their process compared to the alternative instead of complicating it like the comments.