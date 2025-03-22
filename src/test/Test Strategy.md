# Test Strategy
## Pipes
Unit test (Verification).

We check that individual data representations cannot be represented in unexpected ways and forms correctly.
Pipes tend to be easy to test in isolation due to their simplicity so unit tests are quite fitting.

## Filters
Integration test (Verification).

We set up a pipeline (series of pipes and filters) up to and including the filter under test.
We then test the filter output to check it matches our expectations.
Integration testing is good for filters because it is hard to mock their inputs in isolation.

Implementation notes: The "pipeline" should be a private function called by every test case changing only the input diagram.
It returns the pipe output of the filter under test. The oracles verify the contents of that pipe.

## Models
End to end system test (Validation). 

This is where we test from start (visual model input) to finish (specification file output) the system works as expected.
We use real IIAT inputs to compare our system output against to validate that our tool works.
We reproduce the visual models of those systems for the inputs when needed.

## Manual Usecases
Special test cases not part of validation/verification.

We use these as specialized repeatable execution that is useful for debugging but not needed for actual system operation.
As such, these usecases are not themselves tested, and should be used with some caution.
