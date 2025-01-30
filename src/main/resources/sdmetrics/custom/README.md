# Purpose
Keep track of how we represent things in diagrams, how they transform and so on.

## State Diagrams
Note: Only track relevant information (abstracting), 
there should be more information in the XMI in reality.

## Assumptions
1. State Machine names == Agent names AND Agent Names are unique.
   - We use state machine names as a key to associate elements to their diagram. 
2. Transition Names follow this format: inputStim\[guard(optional)]/outStim
   - This is to simplify implementation, and model tool use. "Proper" field setting is tedious.

### Metadata
| Element         | XMI Type     | Can Contain           | Attributes | 
|-----------------|--------------|-----------------------|------------|
| packagedElement | StateMachine | subvertex, transition | name       |
| packagedElement | SignalEvent  | N/A                   | signal     |
| packagedElement | Signal       | N/A                   | name       |

Note: Signals are out of scope of the initial implementation, see "transitions" section below.

### State
| Element    | XMI Type    | Can Contain        | Attributes | 
|------------|-------------|--------------------|------------|
| subvertex  | Pseudostate | N/A                | kind (*1)  |  
| subvertex  | State       | doActivity, region | name       |  
| doActivity | Activity    |                    | name       | 
| region     | Region      | subvertex          | N/A        |   

(*1): Kind is the type of pseudo-state. 
The only kind we care about is "initial", but papyrus does not set the kind field for initial states...
**Assume** for now that when it's not set, it's an inital state, it seems papyrus sets the kind field for the others.

### Transition
| Element       | XMI Type              | Can Contain                | Attributes                | 
|---------------|-----------------------|----------------------------|---------------------------|
| transition    | Transition            | ownedRule, effect, trigger | **source, target**, guard |
| effect        | Activity              | N/A                        | name                      |
| ownedRule     | InteractionConstraint | Specification              |                           |
| specification | Expression            | N/A                        | symbol                    |
| trigger       | Trigger               | N/A                        | event                     |

To simplify implementation, I think it's better we do name String parsing over "proper" transition parsing. 
It's also really annoying as a modeller to set up all the elements, 
a specific name convention is more intuitive and easy to follow than "proper" field setup.

This would limit the scope initially to just transition, and its source/target fields.